package co.edu.cesde.ga.application.service;

import co.edu.cesde.ga.application.dto.StudentDTO;

import java.util.List;

public interface StudentService {
    StudentDTO create(StudentDTO dto);
    StudentDTO findById(Long id);
    List<StudentDTO> findAll();
    StudentDTO update(Long id, StudentDTO dto);
    void delete(Long id);
}
