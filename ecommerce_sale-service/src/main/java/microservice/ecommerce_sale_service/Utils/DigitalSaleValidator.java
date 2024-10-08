package microservice.ecommerce_sale_service.Utils;

import at.backend.drugstore.microservice.common_classes.DTOs.Sale.DigitalSaleItemInsertDTO;
import org.springframework.stereotype.Component;

@Component
public class DigitalSaleValidator {

    public void validateSaleCreation(DigitalSaleItemInsertDTO dto) {
        if (dto == null || dto.getOrderItemDTOS() == null || dto.getOrderItemDTOS().isEmpty()) {
            throw new IllegalArgumentException("Sale must contain at least one item");
        }
    }
}
