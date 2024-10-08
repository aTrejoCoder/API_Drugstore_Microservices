package microservice.employee_service.Service;

import at.backend.drugstore.microservice.common_classes.DTOs.Employee.EmployeeInsertDTO;
import at.backend.drugstore.microservice.common_classes.DTOs.Employee.EmployeeDTO;
import at.backend.drugstore.microservice.common_classes.DTOs.Employee.EmployeeUpdateDTO;
import at.backend.drugstore.microservice.common_classes.DTOs.User.RequestEmployeeUser;
import at.backend.drugstore.microservice.common_classes.Utils.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {
    Result<Void> createEmployee(EmployeeInsertDTO employeeDTO);
    Page<EmployeeDTO> getEmployeesByPagesSortedByName(Pageable pageable);
    Result<EmployeeDTO> getEmployeeById(Long employeeId);
    Result<Void> updateEmployee(EmployeeUpdateDTO employeeUpdateDTO);
    Result<Void> deleteEmployee(Long employeeId);
    Result<EmployeeDTO> getEmployeeByEmailOrPhoneOrID(RequestEmployeeUser requestEmployeeUser);
    boolean validateExisitingEmployee(Long employeeId);
}
