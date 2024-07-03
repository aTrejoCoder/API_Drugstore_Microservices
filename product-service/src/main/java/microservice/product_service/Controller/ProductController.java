package microservice.product_service.Controller;

import at.backend.drugstore.microservice.common_models.DTO.Product.ProductInsertDTO;
import at.backend.drugstore.microservice.common_models.DTO.Product.ProductDTO;
import at.backend.drugstore.microservice.common_models.Utils.Result;
import at.backend.drugstore.microservice.common_models.Validations.ControllerValidation;
import microservice.product_service.Service.ProductServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/api/products")
public class ProductController {

    private final ProductServiceImpl productServiceImpl;
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    public ProductController(ProductServiceImpl productServiceImpl) {
        this.productServiceImpl = productServiceImpl;
    }

    /**
     * Retrieves all products.
     *
     * @return ResponseEntity with the list of product DTOs
     */
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> productDTOS = productServiceImpl.getAllProducts();
        logger.info("All products retrieved successfully");
        return ResponseEntity.ok(productDTOS);
    }

    /**
     * Retrieves products by their IDs.
     *
     * @param productIds the list of product IDs
     * @return ResponseEntity with the list of product DTOs
     */
    @GetMapping("/by-ids")
    public ResponseEntity<List<ProductDTO>> getProductsById(@RequestParam List<Long> productIds) {
        List<ProductDTO> productDTOS = productServiceImpl.getProductsById(productIds);
        logger.info("Products retrieved for IDs: {}", productIds);
        return ResponseEntity.ok(productDTOS);
    }

    /**
     * Retrieves a product by ID.
     *
     * @param productId the ID of the product
     * @return ResponseEntity with the product DTO
     */
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long productId) {
        ProductDTO productDTO = productServiceImpl.getProductById(productId);
        if (productDTO == null) {
            logger.warn("Product not found for ID: {}", productId);
            return ResponseEntity.notFound().build();
        }
        logger.info("Product found for ID: {}", productId);
        return ResponseEntity.ok(productDTO);
    }

    /**
     * Retrieves products by supplier ID.
     *
     * @param supplierId the ID of the supplier
     * @return ResponseEntity with the list of product DTOs
     */
    @GetMapping("/by-supplier/{supplierId}")
    public ResponseEntity<List<ProductDTO>> getProductsBySupplier(@PathVariable Long supplierId) {
        List<ProductDTO> productDTOS = productServiceImpl.FindProductsBySupplier(supplierId);
        logger.info("Products retrieved for supplier ID: {}", supplierId);
        return ResponseEntity.ok(productDTOS);
    }

    /**
     * Retrieves products by category ID.
     *
     * @param categoryId the ID of the category
     * @return ResponseEntity with the list of product DTOs
     */
    @GetMapping("/by-category/{categoryId}")
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@PathVariable Long categoryId) {
        List<ProductDTO> productDTOS = productServiceImpl.findProductsByCategoryId(categoryId);
        logger.info("Products retrieved for category ID: {}", categoryId);
        return ResponseEntity.ok(productDTOS);
    }

    /**
     * Retrieves products by subcategory ID.
     *
     * @param subcategoryId the ID of the subcategory
     * @return ResponseEntity with the list of product DTOs
     */
    @GetMapping("/by-subcategory/{subcategoryId}")
    public ResponseEntity<List<ProductDTO>> getProductsBySubCategory(@PathVariable Long subcategoryId) {
        List<ProductDTO> productDTOS = productServiceImpl.findProductsBySubCategory(subcategoryId);
        logger.info("Products retrieved for subcategory ID: {}", subcategoryId);
        return ResponseEntity.ok(productDTOS);
    }

    /**
     * Inserts a new product.
     *
     * @param productInsertDTO the product insert DTO
     * @param bindingResult    the binding result for validation
     * @return ResponseEntity with a status message
     */
    @PostMapping
    public ResponseEntity<?> insertProduct(@Valid @RequestBody ProductInsertDTO productInsertDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            var validationErrors = ControllerValidation.handleValidationError(bindingResult);
            logger.error("Invalid data provided for inserting product");
            return ResponseEntity.badRequest().body(validationErrors);
        }

        Result<Void> insertResult = productServiceImpl.processInsertProduct(productInsertDTO);
        if (!insertResult.isSuccess()) {
            logger.error("Invalid data provided for process product");
            return ResponseEntity.badRequest().body(insertResult.getErrorMessage());
        }

        logger.info("Product inserted successfully: {}", productInsertDTO.getName());
        return ResponseEntity.ok("Product inserted successfully");
    }

    /**
     * Updates an existing product.
     *
     * @param id               the ID of the product
     * @param productInsertDTO the product insert DTO
     * @param bindingResult    the binding result for validation
     * @return ResponseEntity with a status message
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductInsertDTO productInsertDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.error("Invalid data provided for updating product");
            return ResponseEntity.badRequest().body("Invalid data");
        }

        Result<Void> updatedResult = productServiceImpl.updateProduct(id, productInsertDTO);
        if (!updatedResult.isSuccess()) {
            logger.warn("Invalid data provided for process updating product");
            return ResponseEntity.notFound().build();
        }
        logger.info("Product updated successfully, ID: {}", id);
        return ResponseEntity.ok("Product updated successfully");
    }

    /**
     * Deletes a product by ID.
     *
     * @param id the ID of the product
     * @return ResponseEntity with a status message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        boolean deleted = productServiceImpl.deleteProduct(id);
        if (!deleted) {
            logger.warn("Product not found for deletion, ID: {}", id);
            return ResponseEntity.notFound().build();
        }
        logger.info("Product deleted successfully, ID: {}", id);
        return ResponseEntity.ok("Product deleted successfully");
    }
}
