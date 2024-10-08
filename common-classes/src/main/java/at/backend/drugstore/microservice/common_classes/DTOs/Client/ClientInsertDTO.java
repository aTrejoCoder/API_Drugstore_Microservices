package at.backend.drugstore.microservice.common_classes.DTOs.Client;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class ClientInsertDTO {

    @JsonProperty("first_name")
    @NotNull(message = "first_name is obligatory")
    @NotBlank(message = "first_name can't be blank")
    private String firstName;

    @JsonProperty("last_name")
    @NotNull(message = "last_name is obligatory")
    @NotBlank(message = "last_name can't be blank")
    private String lastName;

    @JsonProperty("birthdate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthdate;

    @JsonProperty("email")
    @NotNull(message = "email is obligatory")
    @NotBlank(message = "email can't be blank")
    private String email;

    @JsonProperty("phone_number")
    private String phoneNumber;
}

