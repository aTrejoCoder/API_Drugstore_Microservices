package at.backend.drugstore.microservice.common_classes.DTOs.Cart;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

@Data
@NoArgsConstructor
public class PurchaseFromCartDTO {
    @JsonProperty("address_id")
    @NotNull(message = "address_id is obligatory")
    @Positive(message = "address_id must be positive")
    private Long addressId;

    @JsonProperty("payment_method")
    @NotNull(message = "payment_method is obligatory")
    @NotBlank(message = "payment_method can not be empty")
    private String paymentMethod;

    @JsonProperty("card_id")
    private Long cardId;
    }
