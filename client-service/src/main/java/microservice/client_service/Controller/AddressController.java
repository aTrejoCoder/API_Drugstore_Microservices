package microservice.client_service.Controller;

import at.backend.drugstore.microservice.common_models.DTO.Client.Adress.AddressInsertDTO;
import at.backend.drugstore.microservice.common_models.DTO.Client.Adress.AddressDTO;
import at.backend.drugstore.microservice.common_models.Utils.ApiResponse;
import microservice.client_service.Service.AddressServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("v1/api/clients/address")
public class AddressController {

    private final AddressServiceImpl addressService;

    @Autowired
    public AddressController(AddressServiceImpl addressService) {
        this.addressService = addressService;
    }

    @PostMapping("/add")
    public CompletableFuture<ResponseEntity<ApiResponse<?>>> insertAddress(@Valid @RequestBody AddressInsertDTO addressInsertDTO,
                                                                           @RequestParam Long clientId) {
        return addressService.addAddress(addressInsertDTO, clientId)
                .thenApply(result -> {
                    if (!result.isSuccess()) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(new ApiResponse<>(false, null, result.getErrorMessage(), 404));
                    }
                    return ResponseEntity.status(HttpStatus.CREATED)
                            .body(new ApiResponse<>(true, null, "Address Successfully Created!.", 201));
                });
    }

    @GetMapping("{addressId}")
    public CompletableFuture<ResponseEntity<ApiResponse<AddressDTO>>> getAddressById(@PathVariable Long addressId) {
        return addressService.getAddressById(addressId)
                .thenApply(result -> {
                    if (!result.isSuccess()) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(new ApiResponse<>(false, null, "Address Not Found", 404));
                    }
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ApiResponse<>(true, result.getData(), "Address Successfully Fetched", 200));
                });
    }

    @GetMapping("client/{clientId}")
    public CompletableFuture<ResponseEntity<ApiResponse<List<AddressDTO>>>> getAddressesByClientId(@PathVariable Long clientId) {
        return addressService.getAddressesByClientId(clientId)
                .thenApply(result -> {
                    if (!result.isSuccess()) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(new ApiResponse<>(false, null, result.getErrorMessage(), 404));
                    }
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ApiResponse<>(true, result.getData(), "Addresses Correctly Fetched", 200));
                });
    }

    @PutMapping("client/address/update/{addressId}")
    public CompletableFuture<ResponseEntity<ApiResponse<Void>>> updateAddressById(@RequestBody AddressInsertDTO addressInsertDTO, @PathVariable Long addressId) {
        return addressService.updateAddressById(addressInsertDTO, addressId)
                .thenApply(result -> {
                    if (!result.isSuccess()) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(new ApiResponse<>(false, null, result.getErrorMessage(), 404));
                    }
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ApiResponse<>(true, null, "Address Successfully Updated.", 200));
                });
    }

    @DeleteMapping("client/address/remove/{addressId}")
    public CompletableFuture<ResponseEntity<ApiResponse<Void>>> deleteAddressById(@PathVariable Long addressId) {
        return addressService.deleteAddressById(addressId)
                .thenApply(result -> {
                    if (!result.isSuccess()) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(new ApiResponse<>(false, null, result.getErrorMessage(), 404));
                    }
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ApiResponse<>(true, null, result.getData(), 200));
                });
    }
}