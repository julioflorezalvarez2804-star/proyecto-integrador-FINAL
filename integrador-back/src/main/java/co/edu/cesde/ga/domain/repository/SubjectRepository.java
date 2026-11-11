package co.edu.cesde.ga.domain.repository;

import co.edu.cesde.ga.domain.model.Subject;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository {
    Subject save(Subject entity);
    Optional<Subject> findById(Long id);
    List<Subject> findAll();
    void deleteById(Long id);
    boolean existsById(Long id);
    boolean existsByCode(String code);
}
