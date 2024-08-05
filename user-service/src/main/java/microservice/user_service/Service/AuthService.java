package microservice.user_service.Service;

import at.backend.drugstore.microservice.common_models.DTO.User.ClientLoginDTO;
import at.backend.drugstore.microservice.common_models.DTO.User.ClientSignUpDTO;
import at.backend.drugstore.microservice.common_models.DTO.User.UserLoginDTO;
import at.backend.drugstore.microservice.common_models.Utils.Result;

import java.util.concurrent.CompletableFuture;

public interface AuthService {
    CompletableFuture<Result<Void>> validateUniqueFields(ClientSignUpDTO clientSignUpDTO);
    CompletableFuture<String> processSignup(ClientSignUpDTO clientSignUpDTO);
    CompletableFuture<String> processLogin(UserLoginDTO userDTO);
    CompletableFuture<Result<UserLoginDTO>> findUser(ClientLoginDTO clientLoginDTO);

    }
