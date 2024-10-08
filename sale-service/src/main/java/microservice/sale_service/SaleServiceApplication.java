package microservice.sale_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableEurekaServer
@ComponentScan(basePackages = {"microservice.sale_service",
		"at.backend.drugstore.microservice.common_classes.GlobalFacadeService.Products",
		"at.backend.drugstore.microservice.common_classes.GlobalFacadeService.Employee" ,
		"at.backend.drugstore.microservice.common_classes.GlobalFacadeService.Inventory"})
public class SaleServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SaleServiceApplication.class, args);
	}

}