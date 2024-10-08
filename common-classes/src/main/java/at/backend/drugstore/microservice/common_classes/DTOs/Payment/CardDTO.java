package at.backend.drugstore.microservice.common_classes.DTOs.Payment;

import at.backend.drugstore.microservice.common_classes.Utils.MonthYearDeserializer;
import at.backend.drugstore.microservice.common_classes.Utils.MonthYearSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class CardDTO {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("client_id")
    private Long clientId;

    @JsonProperty("card_number")
    private String cardNumber;

    @JsonProperty("cardholder_name")
    private String cardholderName;

    @JsonProperty("expiration_date")
    @JsonSerialize(using = MonthYearSerializer.class)
    @JsonDeserialize(using = MonthYearDeserializer.class)
    private LocalDate expirationDate;

    @JsonProperty("cvv")
    private String cvv;


}
