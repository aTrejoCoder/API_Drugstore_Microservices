package microservice.sale_service.Mappers;

import at.backend.drugstore.microservice.common_classes.DTOs.Employee.EmployeeDTO;
import at.backend.drugstore.microservice.common_classes.DTOs.Sale.CreateSaleDTO;
import at.backend.drugstore.microservice.common_classes.DTOs.Sale.ProcessSaleDTO;
import at.backend.drugstore.microservice.common_classes.DTOs.Sale.SaleDTO;
import at.backend.drugstore.microservice.common_classes.DTOs.Sale.SaleInsertDTO;
import microservice.sale_service.Model.PhysicalSale;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SaleMapper {

    SaleMapper INSTANCE = Mappers.getMapper(SaleMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "saleDate", target = "saleDate")
    @Mapping(source = "saleStatus", target = "saleStatus")
    @Mapping(source = "discount", target = "discount")
    @Mapping(source = "total", target = "total")
    @Mapping(source = "clientId", target = "clientId")
    @Mapping(source = "employeeId", target = "employeeId")
    List<SaleDTO> salesToDTOs(List<PhysicalSale> sales);

    @Mapping(source = "id", target = "saleId")
    @Mapping(source = "saleDate", target = "saleDate")
    @Mapping(source = "saleStatus", target = "saleStatus")
    @Mapping(source = "total", target = "totalAmount")
    @Mapping(source = "employeeId", target = "cashierId")
    @Mapping(expression = "java(sale.getEmployeeName())", target = "cashierName")
    @Mapping(source = "clientId", target = "clientId")
    CreateSaleDTO saleToCreateSaleDTO(PhysicalSale sale);

    @Mapping(target = "employeeId", ignore = true)
    @Mapping(target = "clientId", ignore = true)
    @Mapping(target = "employeeName", ignore = true)
    @Mapping(target = "saleItems", ignore = true)
    @Mapping(target = "saleDate", expression = "java(java.time.LocalDateTime.now())")
    PhysicalSale SaleInsertDTOtoEntity(SaleInsertDTO saleInsertDTO);


    @Mapping(source = "discount", target = "discount")
    @Mapping(source = "total", target = "total")
    ProcessSaleDTO paidSaleToProcessSaleDTO(PhysicalSale sale);

    @Mapping(target = "saleDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "employeeName", expression = "java(combineEmployeeName(employeeDTO))")
    PhysicalSale employeeDTOtoEntity(EmployeeDTO employeeDTO);

    default String combineEmployeeName(EmployeeDTO employeeDTO) {
        return employeeDTO.getFirstName() + " " + employeeDTO.getLastName();
    }
}