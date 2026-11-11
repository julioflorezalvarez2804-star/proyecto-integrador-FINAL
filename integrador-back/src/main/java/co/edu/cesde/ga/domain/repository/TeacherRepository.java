package co.edu.cesde.ga.domain.repository;

import co.edu.cesde.ga.domain.model.Teacher;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository {
    Teacher save(Teacher entity);
    Optional<Teacher> findById(Long id);
    List<Teacher> findAll();
    void deleteById(Long id);
    boolean existsById(Long id);
    boolean existsByDocumentNumber(String documentNumber);
}
