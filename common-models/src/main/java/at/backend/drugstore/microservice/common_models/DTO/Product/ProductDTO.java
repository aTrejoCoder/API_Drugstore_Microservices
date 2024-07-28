package at.backend.drugstore.microservice.common_models.DTO.Product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("image")
    private String image;

    @JsonProperty("price")
    private BigDecimal price;

    @JsonProperty("upc")
    private String upc;

    @JsonProperty("content")
    private String content;

    @JsonProperty("package_dimension")
    private String packageDimension;

    @JsonProperty("route_of_administration")
    private String routeOfAdministration;

    @JsonProperty("product_presentation")
    private String productPresentation;

    @JsonProperty("product_type")
    private String productType;

    @JsonProperty("prescription_required")
    private boolean prescriptionRequired;

    @JsonProperty("age_usage")
    private String ageUsage;

    @JsonProperty("category")
    private String category;

    @JsonProperty("subcategory")
    private String subcategory;

    @JsonProperty("supplier")
    private String supplier;

    @JsonProperty("main_category")
    private String mainCategory;

    @JsonProperty("brand")
    private String brand;


}