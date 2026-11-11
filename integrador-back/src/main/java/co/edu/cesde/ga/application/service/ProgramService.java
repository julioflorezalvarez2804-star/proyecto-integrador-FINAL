package co.edu.cesde.ga.application.service;

import co.edu.cesde.ga.application.dto.ProgramDTO;

import java.util.List;

public interface ProgramService {
    ProgramDTO create(ProgramDTO dto);
    ProgramDTO findById(Long id);
    List<ProgramDTO> findAll();
    ProgramDTO update(Long id, ProgramDTO dto);
    void delete(Long id);
}
