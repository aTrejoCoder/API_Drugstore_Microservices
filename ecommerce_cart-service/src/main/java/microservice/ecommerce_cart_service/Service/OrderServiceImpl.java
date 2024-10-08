package microservice.ecommerce_cart_service.Service;

import at.backend.drugstore.microservice.common_classes.DTOs.Cart.CartDTO;
import at.backend.drugstore.microservice.common_classes.DTOs.Cart.ClientEcommerceDataDTO;
import at.backend.drugstore.microservice.common_classes.DTOs.Client.Adress.AddressDTO;
import at.backend.drugstore.microservice.common_classes.DTOs.Client.ClientDTO;
import at.backend.drugstore.microservice.common_classes.DTOs.Order.OrderInsertDTO;
import at.backend.drugstore.microservice.common_classes.DTOs.Payment.CardDTO;
import at.backend.drugstore.microservice.common_classes.DTOs.Payment.PaymentDTO;
import at.backend.drugstore.microservice.common_classes.DTOs.Payment.PaymentInsertDTO;
import at.backend.drugstore.microservice.common_classes.GlobalFacadeService.Address.AddressFacadeService;
import at.backend.drugstore.microservice.common_classes.GlobalFacadeService.Client.ClientFacadeService;
import at.backend.drugstore.microservice.common_classes.GlobalFacadeService.Order.OrderFacadeService;
import at.backend.drugstore.microservice.common_classes.GlobalFacadeService.Payment.EPaymentFacadeService;
import at.backend.drugstore.microservice.common_classes.Utils.Result;
import microservice.ecommerce_cart_service.Mappers.OrderDtoMapper;
import microservice.ecommerce_cart_service.Mappers.PaymentDtoMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class OrderServiceImpl implements OrderService {

    private final EPaymentFacadeService ePaymentFacadeService;
    private final ClientFacadeService clientFacadeService;
    private final AddressFacadeService addressFacadeService;
    private final OrderFacadeService orderFacadeService;
    private final PaymentDtoMapper paymentDtoMapper;
    private final CartService cartService;
    private final OrderDtoMapper orderDtoMapper;

    public OrderServiceImpl(EPaymentFacadeService ePaymentFacadeService,
                            @Qualifier("clientFacadeService") ClientFacadeService clientFacadeService,
                            AddressFacadeService addressFacadeService,
                            OrderFacadeService orderFacadeService,
                            PaymentDtoMapper paymentDtoMapper,
                            CartService cartService,
                            OrderDtoMapper orderDtoMapper) {
        this.ePaymentFacadeService = ePaymentFacadeService;
        this.clientFacadeService = clientFacadeService;
        this.addressFacadeService = addressFacadeService;
        this.orderFacadeService = orderFacadeService;
        this.paymentDtoMapper = paymentDtoMapper;
        this.cartService = cartService;
        this.orderDtoMapper = orderDtoMapper;
    }

    @Override
    public PaymentDTO initPayment(ClientEcommerceDataDTO clientData, Long cardId, Long orderId) {
            // Find the card using cardId
            CardDTO cardDTO = clientData.getCardDTOS().stream()
                    .filter(card -> card.getId().equals(cardId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Card Not Found"));

            // Create the payment insert DTOs
            PaymentInsertDTO paymentInsertDTO = paymentDtoMapper.createPaymentInsertDTO(clientData.getCartDTO(), clientData.getClientDTO(), cardDTO, orderId);
            var paymentDTOCompletableFuture = ePaymentFacadeService.initPayment(paymentInsertDTO);
            return paymentDTOCompletableFuture.join();
    }

    @Override
    @Transactional
    @Async("taskExecutor")
    public CompletableFuture<Result<ClientEcommerceDataDTO>> aggregateClientData(Long clientId) {
        // Start fetching data asynchronously
        CompletableFuture<ClientDTO> clientFuture = clientFacadeService.getClientById(clientId);
        CompletableFuture<Result<List<AddressDTO>>> addressFuture = addressFacadeService.getAddressesByClientId(clientId);
        CompletableFuture<Result<List<CardDTO>>> cardFuture = ePaymentFacadeService.getCardByClientId(clientId);
        CompletableFuture<CartDTO> cartFuture = CompletableFuture.supplyAsync(() -> cartService.getCartByClientId(clientId));

        return CompletableFuture.allOf(clientFuture, addressFuture, cardFuture, cartFuture)
                .thenCompose(v -> {
                    // Check all results and map them
                    ClientDTO clientDTO = clientFuture.join();
                    if (clientDTO == null) {
                        return CompletableFuture.completedFuture(Result.error("Can't retrieve Client data"));
                    }

                    Result<List<AddressDTO>> addressResult = addressFuture.join();
                    if (!addressResult.isSuccess()) {
                        return CompletableFuture.completedFuture(Result.error("Can't retrieve Addresses"));
                    }

                    Result<List<CardDTO>> cardResult = cardFuture.join();
                    if (!cardResult.isSuccess()) {
                        return CompletableFuture.completedFuture(Result.error("Can't retrieve Payment data"));
                    }

                    CartDTO cartDTO = cartFuture.join();
                    if (cartDTO == null || cartDTO.getCartItems().isEmpty()) {
                        return CompletableFuture.completedFuture(Result.error("Cart has no items."));
                    }

                    // Create and populate the result DTOs
                    ClientEcommerceDataDTO clientEcommerceDataDTO = new ClientEcommerceDataDTO();
                    clientEcommerceDataDTO.setClientDTO(clientDTO);
                    clientEcommerceDataDTO.setAddressDTOS(addressResult.getData());
                    clientEcommerceDataDTO.setCardDTOS(cardResult.getData());
                    clientEcommerceDataDTO.setCartDTO(cartDTO);

                    return CompletableFuture.completedFuture(Result.success(clientEcommerceDataDTO));
                });
    }

    @Override
    @Transactional
    @Async("taskExecutor")
    public CompletableFuture<Long> createOrder(CartDTO cartDTO, ClientDTO clientDTO, Long addressId) {
        OrderInsertDTO orderInsertDTO =  orderDtoMapper.createOrderInsertDTO(cartDTO, clientDTO, addressId);
        return orderFacadeService.createOrderAndGetId(orderInsertDTO)
                .thenApply(orderId -> orderId);
    }
}
