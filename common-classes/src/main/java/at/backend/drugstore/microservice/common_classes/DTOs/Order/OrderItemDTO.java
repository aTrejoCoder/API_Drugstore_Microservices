package at.backend.drugstore.microservice.common_classes.DTOs.Order;


import at.backend.drugstore.microservice.common_classes.DTOs.Sale.SaleItemDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class OrderItemDTO extends SaleItemDTO {
    @JsonProperty("order_id")
    private Long orderId;

}
