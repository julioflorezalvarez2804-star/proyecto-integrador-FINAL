package co.edu.cesde.ga.domain.repository;

import co.edu.cesde.ga.domain.model.Grade;

import java.util.List;
import java.util.Optional;

public interface GradeRepository {
    Grade save(Grade grade);
    Optional<Grade> findById(Long id);
    List<Grade> findAll();
    void deleteById(Long id);
    boolean existsById(Long id);
    Optional<Grade> findByGroupSubjectIdAndStudentId(Long groupSubjectId, Long studentId);
}
