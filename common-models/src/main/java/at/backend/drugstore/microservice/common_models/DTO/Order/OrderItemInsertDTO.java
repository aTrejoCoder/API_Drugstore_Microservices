package at.backend.drugstore.microservice.common_models.DTO.Order;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderItemInsertDTO {

    @JsonProperty("product_id")
    private Long productId;

    @JsonProperty("quantity")
    private int quantity;
}
