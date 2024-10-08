package microservice.ecommerce_payment_service.Controller;

import at.backend.drugstore.microservice.common_classes.DTOs.Payment.CardDTO;
import at.backend.drugstore.microservice.common_classes.DTOs.Payment.CardInsertDTO;
import at.backend.drugstore.microservice.common_classes.Security.AuthSecurity;
import at.backend.drugstore.microservice.common_classes.Utils.ResponseWrapper;
import at.backend.drugstore.microservice.common_classes.Utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import microservice.ecommerce_payment_service.Service.CardService;
import microservice.ecommerce_payment_service.Service.CardValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("v1/drugstore/ecommerce-payments/client-cards")
@Tag(name = "Drugstore Microservice API (E-Payment Service)", description = "Service for managing client cards")
public class ClientCardController {

    private final CardService cardService;
    private final AuthSecurity authSecurity;
    private final CardValidatorService cardValidatorService;

    @Autowired
    public ClientCardController(CardService cardService,
                                AuthSecurity authSecurity,
                                CardValidatorService cardValidatorService) {
        this.cardService = cardService;
        this.authSecurity = authSecurity;
        this.cardValidatorService = cardValidatorService;
    }

    @Operation(summary = "Add a card to a client",
            description = "Validates the client and adds a card to their account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Card successfully added"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @PostMapping("/add")
    public ResponseEntity<ResponseWrapper<Void>> addCard(@Valid @RequestBody CardInsertDTO cardInsertDTO,
                                                         HttpServletRequest request) {
        Long clientId = authSecurity.getClientIdFromToken(request);
        log.info("Fetching card for client ID: {}", clientId);

        boolean isCardNumberValidated = cardValidatorService.validateCard(cardInsertDTO.getCardNumber());
        if (!isCardNumberValidated) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseWrapper.badRequest("Invalid Card Number"));
        }

        Result<Void> addResult = cardService.addCardToClient(cardInsertDTO, clientId);
        if (!addResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseWrapper.badRequest(addResult.getErrorMessage()));
        }
        log.info("Card successfully added for client ID: {}", clientId);

        return ResponseEntity.ok(ResponseWrapper.ok("Card", "Added"));
    }

    @Operation(summary = "Update a card to a client",
            description = "Validates the client and update a card to their account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Card successfully added"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @PostMapping("/{cardId}")
    public ResponseEntity<ResponseWrapper<Void>> updateMyCard(@Valid @RequestBody CardInsertDTO cardInsertDTO,
                                                              HttpServletRequest request,
                                                              @PathVariable Long cardId) {
        Long clientId = authSecurity.getClientIdFromToken(request);
        log.info("updateCard -> Fetching card for client ID: {}", clientId);

        boolean isCardNumberValidated = cardValidatorService.validateCard(cardInsertDTO.getCardNumber());
        if (!isCardNumberValidated) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseWrapper.badRequest("Invalid Card Number"));
        }

        Result<Void> addResult = cardService.updateCard(cardInsertDTO, clientId, cardId);
        if (!addResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseWrapper.badRequest(addResult.getErrorMessage()));
        }
        log.info("updateCard -> Card successfully updated for client ID: {}", clientId);

        return ResponseEntity.ok(ResponseWrapper.ok("Card", "Added"));
    }


    @Operation(summary = "Retrieve cards by client ID",
            description = "Fetches all cards associated with a specific client.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cards retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @GetMapping
    public ResponseEntity<ResponseWrapper<List<CardDTO>>> getMyCards(HttpServletRequest request) {
        Long clientId = authSecurity.getClientIdFromToken(request);
        log.info("getCardsByClientId -> Fetching cards for client ID: {}", clientId);

        List<CardDTO> cardDTOS = cardService.getCardByClientId(clientId);
        log.info("Cards retrieved successfully for client ID: {}", clientId);

        return ResponseEntity.ok(ResponseWrapper.found(cardDTOS, "Card"));
    }

    @Operation(summary = "Delete a card by ID",
            description = "Deletes a specific card from the client's account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Card not found"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @DeleteMapping("/{cardId}")
    public ResponseEntity<ResponseWrapper<Void>> deleteCard(@PathVariable Long cardId,
                                                                HttpServletRequest request) {
        Long clientId = authSecurity.getClientIdFromToken(request);
        log.info("deleteCardById -> Request to delete card ID: {} for client ID: {}", cardId, clientId);

        boolean isClientExisting = cardValidatorService.validateExistingCard(cardId);
        if (!isClientExisting) {
            log.warn("Card with ID {} not found.", cardId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound("Card", "Id"));
        }

        cardService.deleteCardById(cardId);
        log.info("Card with ID {} successfully deleted for client ID: {}", cardId, clientId);

        return ResponseEntity.ok(ResponseWrapper.ok("Card", "Delete"));
    }
}
