package co.edu.cesde.ga.application.service.impl;

import co.edu.cesde.ga.application.dto.PeriodDTO;
import co.edu.cesde.ga.application.service.PeriodService;
import co.edu.cesde.ga.domain.exception.BusinessException;
import co.edu.cesde.ga.domain.exception.PeriodAlreadyExistsException;
import co.edu.cesde.ga.domain.exception.PeriodNotFoundException;
import co.edu.cesde.ga.domain.model.Period;
import co.edu.cesde.ga.domain.repository.PeriodRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PeriodServiceImpl implements PeriodService {

    private final PeriodRepository periodRepository;

    public PeriodServiceImpl(PeriodRepository periodRepository) {
        this.periodRepository = periodRepository;
    }

    @Override
    public PeriodDTO create(PeriodDTO dto) {
        validateDates(dto);
        if (periodRepository.existsByCode(dto.getCode())) {
            throw new PeriodAlreadyExistsException(dto.getCode());
        }
        Period period = new Period(null, dto.getCode(), dto.getStartDate(), dto.getEndDate());
        return toDTO(periodRepository.save(period));
    }

    @Override
    public PeriodDTO findById(Long id) {
        Period period = periodRepository.findById(id)
                .orElseThrow(() -> new PeriodNotFoundException(id));
        return toDTO(period);
    }

    @Override
    public List<PeriodDTO> findAll() {
        return periodRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public PeriodDTO update(Long id, PeriodDTO dto) {
        if (!periodRepository.existsById(id)) {
            throw new PeriodNotFoundException(id);
        }
        validateDates(dto);
        Period period = new Period(id, dto.getCode(), dto.getStartDate(), dto.getEndDate());
        return toDTO(periodRepository.save(period));
    }

    @Override
    public void delete(Long id) {
        if (!periodRepository.existsById(id)) {
            throw new PeriodNotFoundException(id);
        }
        periodRepository.deleteById(id);
    }

    private void validateDates(PeriodDTO dto) {
        if (dto.getStartDate() != null && dto.getEndDate() != null
                && dto.getEndDate().isBefore(dto.getStartDate())) {
            throw new BusinessException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }
    }

    private PeriodDTO toDTO(Period period) {
        return new PeriodDTO(period.getPeriodId(), period.getCode(), period.getStartDate(), period.getEndDate());
    }
}
