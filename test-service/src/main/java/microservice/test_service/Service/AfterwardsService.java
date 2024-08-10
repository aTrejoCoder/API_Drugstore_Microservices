package microservice.test_service.Service;

import at.backend.drugstore.microservice.common_models.DTOs.Cart.CartItemDTO;
import at.backend.drugstore.microservice.common_models.Utils.Result;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface AfterwardsService {
    CompletableFuture<Result<Void>> moveProductToAfterwards(Long clientId, Long productId);
    CompletableFuture<Result<Void>> returnProductToCart(Long clientId, Long productId);
    CompletableFuture<List<CartItemDTO>> getAfterwardsByClientId(Long clientId);
    CompletableFuture<Optional<CartItemDTO>> getAfterwardsBytId(Long afterwardsId);
}
