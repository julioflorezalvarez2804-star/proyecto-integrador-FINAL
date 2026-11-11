package co.edu.cesde.ga.application.service;

import co.edu.cesde.ga.application.dto.EnrollmentDTO;

import java.util.List;

public interface EnrollmentService {
    EnrollmentDTO enroll(Long studentId, Long groupId);
    EnrollmentDTO findById(Long id);
    List<EnrollmentDTO> findAll();
    void cancel(Long id);
    void complete(Long id);
    void delete(Long id);
}