package at.backend.drugstore.microservice.common_models.ExternalService.Employee;

import at.backend.drugstore.microservice.common_models.DTO.Employee.EmployeeDTO;
import at.backend.drugstore.microservice.common_models.DTO.Sale.SaleProductsDTO;
import at.backend.drugstore.microservice.common_models.Utils.Result;
import org.springframework.stereotype.Service;

@Service
public interface ExternalEmployeeService {
    Result<EmployeeDTO> getEmployeeBySaleProductsDTO(SaleProductsDTO saleProductsDTO);
    Result<EmployeeDTO> findEmployeeById(Long employeeId);
}
