package microservice.ecommerce_cart_service.Service.DomainService;

import at.backend.drugstore.microservice.common_classes.Utils.Result;
import microservice.ecommerce_cart_service.Model.Afterward;
import microservice.ecommerce_cart_service.Model.Cart;

import java.util.concurrent.CompletableFuture;

public interface AfterwardsDomainService {
    void processReturnToAfterwards(Cart cart, Afterward afterward);
    Result<Void> processMoveToAfterwards(Cart cart, Long productId, Long clientId);
}
