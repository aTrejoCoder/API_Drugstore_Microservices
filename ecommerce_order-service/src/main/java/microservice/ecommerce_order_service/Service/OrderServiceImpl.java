package microservice.ecommerce_order_service.Service;

import at.backend.drugstore.microservice.common_classes.DTOs.Cart.CartDTO;
import at.backend.drugstore.microservice.common_classes.DTOs.Client.Adress.AddressDTO;
import at.backend.drugstore.microservice.common_classes.DTOs.Client.ClientDTO;
import at.backend.drugstore.microservice.common_classes.DTOs.Order.CompleteOrderRequest;
import at.backend.drugstore.microservice.common_classes.DTOs.Order.OrderDTO;
import at.backend.drugstore.microservice.common_classes.DTOs.Order.OrderInsertDTO;
import at.backend.drugstore.microservice.common_classes.DTOs.Order.OrderStatus;
import at.backend.drugstore.microservice.common_classes.GlobalFacadeService.Client.AddressFacadeService;
import at.backend.drugstore.microservice.common_classes.GlobalFacadeService.Client.AddressFacadeServiceImpl;
import at.backend.drugstore.microservice.common_classes.GlobalFacadeService.Client.ClientFacadeService;
import at.backend.drugstore.microservice.common_classes.Utils.Result;
import lombok.extern.slf4j.Slf4j;
import microservice.ecommerce_order_service.Mapper.OrderItemMapper;
import microservice.ecommerce_order_service.Mapper.OrderMapper;
import microservice.ecommerce_order_service.Model.CompleteOrderData;
import microservice.ecommerce_order_service.Model.Order;
import microservice.ecommerce_order_service.Model.ShippingData;
import microservice.ecommerce_order_service.Repository.OrderItemRepository;
import microservice.ecommerce_order_service.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final ClientFacadeService clientFacadeServiceFacade;
    private final OrderDomainService orderDomainService;
    private final ShippingService shippingService;
    private final AddressFacadeService addressFacadeServiceImpl;

    public OrderServiceImpl(OrderRepository orderRepository,
                            OrderItemRepository orderItemRepository,
                            OrderMapper orderMapper,
                            OrderItemMapper orderItemMapper,
                            @Qualifier("clientFacadeService") ClientFacadeService clientFacadeService,
                            OrderDomainService orderDomainService,
                            ShippingService shippingService,
                            AddressFacadeServiceImpl addressFacadeServiceImpl) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.clientFacadeServiceFacade = clientFacadeService;
        this.orderDomainService = orderDomainService;
        this.shippingService = shippingService;
        this.addressFacadeServiceImpl = addressFacadeServiceImpl;
    }


    @Override
    @Transactional
    public Order createOrder(OrderInsertDTO orderInsertDTO) {
            Order order = orderDomainService.createOrder(orderInsertDTO, orderMapper, orderItemMapper);
            order = orderRepository.save(order);
            orderItemRepository.saveAll(order.getItems());
            return order;
    }

    @Override
    @Transactional
    public OrderDTO processOrderCreation(Order order, Long clientId, CartDTO cartDTO) {
        order.setClientId(clientId);
        order.setItems(orderDomainService.generateOrderItems(cartDTO.getCartItems(), order, orderItemMapper));
        return orderMapper.entityToDTO(order);
    }

    @Override
    @Transactional
    public void processOrderNotPaid(Long orderId) {
            Optional<Order> optionalOrder = orderRepository.findById(orderId);
            if(optionalOrder.isEmpty()) {
                throw new RuntimeException();
            }
            Order order = optionalOrder.get();
            order.setStatus(OrderStatus.PAID_FAILED);
            order.setLastOrderUpdate(LocalDateTime.now());
            orderRepository.saveAndFlush(order);
    }

    @Override
    @Transactional
    public Result<Void> processOrderPayment(CompleteOrderRequest completeOrderRequest, AddressDTO addressDTO, ClientDTO clientDTO, OrderDTO orderDTO) {
        Optional<Order> optionalOrder = orderRepository.findById(completeOrderRequest.getOrderId());
        if (optionalOrder.isEmpty()) {
            return Result.error("Order not found");
        }

        Order order = optionalOrder.get();
        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            return Result.error("Order already processed");
        }

        // Create shipping data asynchronously and handle the completion
        processOrderPaid(clientDTO, order, addressDTO);

        return Result.success();
    }

    @Override
    @Transactional
    public Optional<OrderDTO> getOrderById(Long orderId) {
            Optional<Order> optionalOrder = orderRepository.findById(orderId);
            return optionalOrder.map(orderDomainService::makeOrderDTO).map(CompletableFuture::join);
    }

    @Override
    @Transactional
    public String deliveryOrder(Long orderId, boolean isOrderDelivered) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("Order not found"));

        String result = orderDomainService.handleDelivery(order, isOrderDelivered);
        orderRepository.save(order);
        return result;
    }

    @Override
    public Result<Void> validateOrderForDelivery(OrderDTO orderDTO) {
        return orderDTO.getStatus() != OrderStatus.PENDING_PAYMENT ? Result.success() : Result.error("Invalid Order");
    }

    @Override
    @Async("taskExecutor")
    @Transactional
    public CompletableFuture<CompleteOrderData> bringClientDataToCompleteOrder(CompleteOrderRequest completeOrderRequest) {
        CompletableFuture<Result<ClientDTO>> clientFuture = clientFacadeServiceFacade.findClientById(completeOrderRequest.getClientId());
        CompletableFuture<Result<AddressDTO>> addressFuture = addressFacadeServiceImpl.getAddressById(completeOrderRequest.getAddressId());
        CompletableFuture<Optional<Order>> orderFuture = CompletableFuture.supplyAsync(() -> orderRepository.findById(completeOrderRequest.getOrderId()));

        return CompletableFuture.allOf(clientFuture, addressFuture, orderFuture)
                .thenApply(v -> orderDomainService.createCompleteOrderData(clientFuture.join(), addressFuture.join(), orderFuture.join(), orderMapper));
    }

    @Override
    @Transactional
    public void addPaymentIdByOrderId(Long orderId, Long paymentId) {
        orderRepository.findById(orderId)
                .ifPresent(order -> {
                    order.setPaymentId(paymentId);
                    orderRepository.save(order);
                });
    }


    @Transactional
    private void processOrderPaid(ClientDTO clientDTO, Order order, AddressDTO addressDTO) {
        ShippingData shippingData = shippingService.generateShippingData(addressDTO, clientDTO);

        order.setStatus(OrderStatus.TO_BE_DELIVERED);
        order.setLastOrderUpdate(LocalDateTime.now());
        order.setShippingData(shippingData);
        orderRepository.saveAndFlush(order);
    }
}