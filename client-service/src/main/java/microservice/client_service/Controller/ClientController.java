package microservice.client_service.Controller;

import at.backend.drugstore.microservice.common_models.DTO.Client.ClientInsertDTO;
import at.backend.drugstore.microservice.common_models.DTO.Client.ClientDTO;
import at.backend.drugstore.microservice.common_models.Utils.ApiResponse;
import at.backend.drugstore.microservice.common_models.Validations.ControllerValidation;
import microservice.client_service.Service.ClientService;
import microservice.client_service.Service.ClientServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("v1/api/clients")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientServiceImpl clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/add")
    public CompletableFuture<ResponseEntity<ApiResponse<ClientDTO>>> createClient(@Valid @RequestBody ClientInsertDTO clientInsertDTO) {
        return clientService.createClient(clientInsertDTO)
                .thenApply(clientDTO -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ApiResponse<>(true, clientDTO, "Client Successfully Created.", 201)));
    }

    @GetMapping("/{clientId}")
    public CompletableFuture<ResponseEntity<ApiResponse<ClientDTO>>> getClientById(@PathVariable Long clientId) {
        return clientService.getClientById(clientId)
                .thenApply(clientDTO -> {
                    if (clientDTO == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(new ApiResponse<>(false, null, "Client Not Found", 404));
                    }
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ApiResponse<>(true, clientDTO, "Client Successfully Fetched", 200));
                });
    }

    @GetMapping("/all")
    public CompletableFuture<ResponseEntity<ApiResponse<List<ClientDTO>>>> getAllClients() {
        return clientService.getAllClients()
                .thenApply(clientDTOS -> ResponseEntity.status(HttpStatus.OK)
                        .body(new ApiResponse<>(true, clientDTOS, "Clients Successfully Fetched", 200)));
    }

    @DeleteMapping("/remove/{clientId}")
    public CompletableFuture<ResponseEntity<ApiResponse<Void>>> deleteClientById(@PathVariable Long clientId) {
        return clientService.deleteClient(clientId)
                .thenApply(isClientDeleted -> {
                    if (!isClientDeleted) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(new ApiResponse<>(false, null, "Client Not Found", 404));
                    }
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ApiResponse<>(true, null, "Client Successfully Deleted", 200));
                });
    }
}