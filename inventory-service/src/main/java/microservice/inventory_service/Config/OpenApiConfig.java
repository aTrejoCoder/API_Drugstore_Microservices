package microservice.inventory_service.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI inventoryServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Inventory Service API")
                        .description("Inventory Microservice that handles all Inventory Requests")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Codmind")
                                .url("https://codmind.com")
                                .email("apis@codmind.com"))
                        .termsOfService("http://codmind.com/terms")
                );
    }
}