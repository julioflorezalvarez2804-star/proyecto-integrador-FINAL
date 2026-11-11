package co.edu.cesde.ga.application.service;

import co.edu.cesde.ga.application.dto.SubjectDTO;

import java.util.List;

public interface SubjectService {
    SubjectDTO create(SubjectDTO dto);
    SubjectDTO findById(Long id);
    List<SubjectDTO> findAll();
    SubjectDTO update(Long id, SubjectDTO dto);
    void delete(Long id);
}
