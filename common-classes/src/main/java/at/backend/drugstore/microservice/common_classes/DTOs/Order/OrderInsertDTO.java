package at.backend.drugstore.microservice.common_classes.DTOs.Order;

import at.backend.drugstore.microservice.common_classes.DTOs.Cart.CartDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderInsertDTO {

    @NotNull(message = "client_id is obligatory")
    @Positive(message = "client_id must be postive")
    @JsonProperty("client_id")
    private Long clientId;

    @JsonProperty("cart")
    @NotNull(message = "cart is obligatory")
    private CartDTO cartDTO;

    @JsonProperty("address_id")
    @NotNull(message = "address_id is obligatory")
    private Long addressId;
}
