package co.edu.cesde.ga.domain.repository;

import co.edu.cesde.ga.domain.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentRepository {
    Student save(Student student);
    Optional<Student> findById(Long id);
    List<Student> findAll();
    void deleteById(Long id);
    boolean existsById(Long id);
    Optional<Student> findByDocumentNumber(String documentNumber);
}
