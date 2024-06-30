package at.backend.drugstore.microservice.common_models.DTO.Order;

import at.backend.drugstore.microservice.common_models.DTO.Client.ClientDTO;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ShippingInsertDTO {
    public OrderDTO orderDTO;
    public ClientDTO clientDTO;

    public ShippingInsertDTO(OrderDTO orderDTO) {
        this.orderDTO = orderDTO;
    }
}

