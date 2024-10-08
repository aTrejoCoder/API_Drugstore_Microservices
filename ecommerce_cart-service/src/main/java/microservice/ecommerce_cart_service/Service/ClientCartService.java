package microservice.ecommerce_cart_service.Service;


import at.backend.drugstore.microservice.common_classes.DTOs.Cart.CartItemInsertDTO;
import at.backend.drugstore.microservice.common_classes.DTOs.Cart.ClientEcommerceDataDTO;
import at.backend.drugstore.microservice.common_classes.DTOs.Cart.PurchaseFromCartDTO;
import at.backend.drugstore.microservice.common_classes.Utils.Result;

import java.util.concurrent.CompletableFuture;

public interface ClientCartService {
    Result<Void> addProductsCart(Long clientId, Long productId, int quantity);
    Result<Void> deleteProductFromCart(Long clientId, CartItemInsertDTO cartItemInsertDTO);
    CompletableFuture<Void> clearOutCart(Long cartId);
}
