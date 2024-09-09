package microservice.employee_service.Controller;

import at.backend.drugstore.microservice.common_classes.Utils.ResponseWrapper;
import at.backend.drugstore.microservice.common_classes.DTOs.Employee.Postion.PositionInsertDTO;
import at.backend.drugstore.microservice.common_classes.DTOs.Employee.Postion.PositionDTO;
import at.backend.drugstore.microservice.common_classes.DTOs.Employee.Postion.PositionUpdateDTO;
import lombok.extern.slf4j.Slf4j;
import microservice.employee_service.Service.PositionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("v1/drugstore/employees/positions")
@Tag(name = "Drugstore Microservice API (Employee Service)", description = "Service for managing employees positions")
public class PositionController {

    private final PositionService positionService;

    @Autowired
    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    @Operation(summary = "Create a new position")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Position successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/create")
    public ResponseEntity<ResponseWrapper<Void>> createPosition(@Valid @RequestBody PositionInsertDTO positionInsertDTO) {

        positionService.createPosition(positionInsertDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.created("Position"));
    }

    @Operation(summary = "Get all positions sorted by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Positions successfully fetched")
    })
    @GetMapping("/by-name")
    public ResponseEntity<ResponseWrapper<Page<PositionDTO>>> getPositionsOrderByName(@RequestParam(defaultValue = "0") int page,
                                                                                 @RequestParam(defaultValue = "10") int size,
                                                                                 @RequestParam(defaultValue = "true") boolean sortedAsc) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PositionDTO> positionDTOS = positionService.getPositionsSortedByNameAsc(pageable, sortedAsc);

        return ResponseEntity.ok(ResponseWrapper.found(positionDTOS,"Positions"));
    }

    @Operation(summary = "Get position by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Position successfully fetched"),
            @ApiResponse(responseCode = "404", description = "Position not found")
    })
    @GetMapping("/{positionId}")
    public ResponseEntity<ResponseWrapper<PositionDTO>> getPositionById(@PathVariable Long positionId) {
        PositionDTO positionDTO = positionService.getPositionById(positionId);
        if (positionDTO == null) {
            log.warn("Position with ID {} not found.", positionId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseWrapper<>(false, null, "Position with Id " + positionId + " Not Found.", HttpStatus.NOT_FOUND.value()));
        }
        log.info("Fetched position with ID {}.", positionId);
        return ResponseEntity.ok(new ResponseWrapper<>(true, positionDTO, "Position Successfully Fetched.", HttpStatus.OK.value()));
    }

    @Operation(summary = "Update an existing position")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Position successfully updated"),
            @ApiResponse(responseCode = "404", description = "Position not found")
    })
    @PutMapping("/update")
    public ResponseEntity<ResponseWrapper<Void>> updatePosition(@Valid @RequestBody PositionUpdateDTO positionUpdateDTO) {
       boolean isPositionUpdated = positionService.validateExisitingPosition(positionUpdateDTO.getId());
        if (!isPositionUpdated) {
            log.warn("Position with ID {} not found for update.", positionUpdateDTO.getId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound("Position", "Id"));
        }

        positionService.updatePosition(positionUpdateDTO);
        log.info("Position with ID {} successfully updated.", positionUpdateDTO.getId());

        return ResponseEntity.ok(new ResponseWrapper<>(true, null, "Position Successfully Updated.", HttpStatus.OK.value()));
    }

    @Operation(summary = "Delete position by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Position successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Position not found")
    })
    @DeleteMapping("/{positionId}")
    public ResponseEntity<ResponseWrapper<Void>> deletePosition(@PathVariable Long positionId) {
        boolean isPositionExisting = positionService.validateExisitingPosition(positionId);
        if (!isPositionExisting) {
            log.warn("Position with ID {} not found for deletion.", positionId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.notFound("Position", "Id"));
        }

        positionService.deletePosition(positionId);
        log.info("Position with ID {} successfully deleted.", positionId);

        return ResponseEntity.ok(ResponseWrapper.ok("Position", "Delete"));
    }
}
