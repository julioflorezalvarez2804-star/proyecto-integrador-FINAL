package co.edu.cesde.ga.application.service;

import co.edu.cesde.ga.application.dto.PeriodDTO;

import java.util.List;

public interface PeriodService {
    PeriodDTO create(PeriodDTO dto);
    PeriodDTO findById(Long id);
    List<PeriodDTO> findAll();
    PeriodDTO update(Long id, PeriodDTO dto);
    void delete(Long id);
}
