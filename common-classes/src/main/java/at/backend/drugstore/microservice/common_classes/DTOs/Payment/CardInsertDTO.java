package at.backend.drugstore.microservice.common_classes.DTOs.Payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class CardInsertDTO {
    @JsonProperty("card_number")
    @NotNull(message = "card_number is obligatory")
    @NotBlank(message = "card_number is empty")
    @Pattern(regexp = "\\b\\d{16}\\b", message = "card_number must be 16 digits")
    private String cardNumber;

    @JsonProperty("cardholder_name")
    @NotNull(message = "cardholder_name is obligatory")
    @NotBlank(message = "cardholder_name is empty")
    private String cardholderName;

    @JsonProperty("expiration_date")
    @NotNull(message = "expiration_date is obligatory")
    private LocalDate expirationDate;

    @JsonProperty("cvv")
    @NotNull(message = "cvv is obligatory")
    @NotBlank(message = "cvv is empty")
    @Pattern(regexp = "\\b\\d{3}\\b", message = "cvv must be 3 digits")
    private String cvv;

    @JsonProperty("card_type")
    @NotNull(message = "card_type is obligatory")
    @NotBlank(message = "card_type is empty")
    private String cardType;
}
