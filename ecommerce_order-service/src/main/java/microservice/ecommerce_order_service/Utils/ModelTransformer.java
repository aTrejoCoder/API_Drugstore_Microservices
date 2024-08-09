package microservice.ecommerce_order_service.Utils;

import at.backend.drugstore.microservice.common_models.DTOs.Client.Adress.AddressDTO;
import at.backend.drugstore.microservice.common_models.DTOs.Client.ClientDTO;
import at.backend.drugstore.microservice.common_models.DTOs.Order.*;
import microservice.ecommerce_order_service.Model.Order;
import microservice.ecommerce_order_service.Model.OrderItem;
import microservice.ecommerce_order_service.Model.ShippingData;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ModelTransformer {

    public static Order InsertDtoToOrder(OrderInsertDTO orderInsertDTO) {
        Order order = new Order();
        order.setStatus(OrderStatus.PENDING_PAYMENT);
        order.setOrderDate(LocalDateTime.now());
        order.setLastOrderUpdate(LocalDateTime.now());
        order.setDeliveryTries(0);
        order.setClientId(orderInsertDTO.getClientId());

        return order;
    }

    public static ShippingData makeShippingData(AddressDTO addressDTO, ClientDTO clientDTO) {
        ShippingData shippingData = new ShippingData();

        StringBuilder address = new StringBuilder();
        address.append(addressDTO.getStreet());
        address.append(" #").append(addressDTO.getHouseNumber());

        if (addressDTO.getInnerNumber() != null) {
            address.append(" (interior #").append(addressDTO.getInnerNumber()).append(")");
        }

        shippingData.setAddress(address.toString());
        shippingData.setCity(addressDTO.getCity());
        shippingData.setState(addressDTO.getState());
        shippingData.setCountry(addressDTO.getCountry());
        shippingData.setPostalCode(String.valueOf(addressDTO.getZipCode()));
        shippingData.setPhoneNumber(clientDTO.getPhone());
        shippingData.setRecipientName(clientDTO.getFirstName() + " " + clientDTO.getLastName());
        shippingData.setShippingCost(BigDecimal.ZERO);
        shippingData.setTrackingNumber(GenerateTrackingNumber());

        return shippingData;
    }

    public static OrderDTO orderToDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setOrderDate(order.getOrderDate());
        orderDTO.setClientId(order.getClientId());
        orderDTO.setStatus(order.getStatus());

        List<OrderItemDTO> orderItems = orderItemToDTO(order.getItems());
        orderDTO.setItems(orderItems);

        return orderDTO;
    }

    private static List<OrderItemDTO> orderItemToDTO(List<OrderItem> orderItems) {
        List<OrderItemDTO> orderItemDTOS = new ArrayList<>();
        for (var orderitem  : orderItems) {
            OrderItemDTO orderItemDTO = new OrderItemDTO();
            orderItemDTO.setOrderId(orderitem.getProductId());
            orderItemDTO.setProductQuantity(orderitem.getProductQuantity());
            orderItemDTO.setOrderId(orderitem.getOrder().getId());
            orderItemDTO.setProductQuantity(orderitem.getProductQuantity());

            orderItemDTOS.add(orderItemDTO);
        }

        return orderItemDTOS;
    }

    private static String GenerateTrackingNumber() {
        int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0};
        StringBuilder trackingNumber = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            int randomIndex = random.nextInt(numbers.length);
            int randomNumber = numbers[randomIndex];
            trackingNumber.append(randomNumber);
        }

        return trackingNumber.toString();
    }



}
