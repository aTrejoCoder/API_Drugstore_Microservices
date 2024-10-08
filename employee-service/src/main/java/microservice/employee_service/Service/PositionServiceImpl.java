package microservice.employee_service.Service;

import at.backend.drugstore.microservice.common_classes.DTOs.Employee.Postion.PositionInsertDTO;
import at.backend.drugstore.microservice.common_classes.DTOs.Employee.Postion.PositionDTO;
import at.backend.drugstore.microservice.common_classes.DTOs.Employee.Postion.PositionUpdateDTO;
import at.backend.drugstore.microservice.common_classes.Utils.Result;
import lombok.extern.slf4j.Slf4j;
import microservice.employee_service.Mappers.PositionMapper;
import microservice.employee_service.Model.Position;
import microservice.employee_service.Model.enums.ClassificationWorkday;
import microservice.employee_service.Repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;
    private final PositionMapper positionMapper;

    @Autowired
    public PositionServiceImpl(PositionRepository positionRepository, PositionMapper positionMapper) {
        this.positionRepository = positionRepository;
        this.positionMapper = positionMapper;
    }

    @Override
    @Cacheable(value = "positionsByPage", key = "#pageable.pageNumber + '-' + #pageable.pageSize + '-' + #sortedAsc")
    public Page<PositionDTO> getPositionsSortedByNameAsc(Pageable pageable, boolean sortedAsc) {
        Page<Position> positions;
        if (sortedAsc) {
            positions = positionRepository.findAllByOrderByPositionNameAsc(pageable);
        } else  {
            positions = positionRepository.findAllByOrderByPositionNameDesc(pageable);
        }
        return positions.map(positionMapper::entityToDTO);
    }


    @Override
    @Cacheable(value = "positionById", key = "#positionId")
    public PositionDTO getPositionById(Long positionId) {
        Optional<Position> optionalPosition = positionRepository.findById(positionId);
        return optionalPosition.map(positionMapper::entityToDTO).orElse(null);
    }

    @Override
    @Transactional
    public void createPosition(PositionInsertDTO positionInsertDTO) {
        Position position = positionMapper.insertDtoToEntity(positionInsertDTO);
        position.setClassificationWorkday(ClassificationWorkday.FULL_TIME);

        positionRepository.saveAndFlush(position);
    }

    @Override
    @Transactional
    public Result<Void> updatePosition(PositionUpdateDTO positionUpdateDTO) {
        Optional<Position> optionalPosition = positionRepository.findById(positionUpdateDTO.getPositionId());
        return optionalPosition.map(position -> {
            positionMapper.updateDTOtoEntity(positionUpdateDTO, position);
            positionRepository.saveAndFlush(position);

            return Result.success();
        }).orElseGet(() -> Result.error("Position not found"));
    }

    @Override
    @Transactional
    public Result<Void> deletePosition(Long positionId) {
       if (!positionRepository.existsById(positionId)) {
           return Result.error("Position not found");
       }

       positionRepository.deleteById(positionId);
       return Result.success();
    }
}
