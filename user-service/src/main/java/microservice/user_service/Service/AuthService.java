package microservice.user_service.Service;

import at.backend.drugstore.microservice.common_classes.DTOs.User.ClientLoginDTO;
import at.backend.drugstore.microservice.common_classes.DTOs.User.ClientSignUpDTO;
import at.backend.drugstore.microservice.common_classes.DTOs.User.UserLoginDTO;
import at.backend.drugstore.microservice.common_classes.Utils.Result;

import java.util.concurrent.CompletableFuture;

public interface AuthService {
    Result<Void> validateUniqueFields(ClientSignUpDTO clientSignUpDTO);
    String processSignup(ClientSignUpDTO clientSignUpDTO);
    CompletableFuture<String> processLogin(UserLoginDTO userDTO);
    Result<UserLoginDTO> findUser(ClientLoginDTO clientLoginDTO);
    Result<Void> validateLogin(String plainPassword, String hashPassword);
    }
