package co.edu.cesde.ga.application.service;

import co.edu.cesde.ga.application.dto.TeacherDTO;

import java.util.List;

public interface TeacherService {
    TeacherDTO create(TeacherDTO dto);
    TeacherDTO findById(Long id);
    List<TeacherDTO> findAll();
    TeacherDTO update(Long id, TeacherDTO dto);
    void delete(Long id);
}
