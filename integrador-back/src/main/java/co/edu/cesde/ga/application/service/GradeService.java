package co.edu.cesde.ga.application.service;

import co.edu.cesde.ga.application.dto.GradeDTO;

import java.util.List;

public interface GradeService {
    GradeDTO assignGrade(Long groupSubjectId, Long studentId, double finalScore, String observation);
    GradeDTO findById(Long id);
    List<GradeDTO> findAll();
    GradeDTO update(Long id, double finalScore, String observation);
    void delete(Long id);
}
