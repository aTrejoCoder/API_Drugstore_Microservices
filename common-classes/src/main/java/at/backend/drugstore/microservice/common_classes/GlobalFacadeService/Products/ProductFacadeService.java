package at.backend.drugstore.microservice.common_classes.GlobalFacadeService.Products;

import at.backend.drugstore.microservice.common_classes.DTOs.Product.ProductDTO;
import at.backend.drugstore.microservice.common_classes.Utils.Result;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ProductFacadeService {
    CompletableFuture<List<ProductDTO>> getProductsByIds(List<Long> productIds);
    CompletableFuture<Result<ProductDTO>> getProductById(Long productId);
    CompletableFuture<Boolean> validateExistingProduct(Long productId);
    CompletableFuture<Boolean> validateExistingSupplier(Long supplierId);
}