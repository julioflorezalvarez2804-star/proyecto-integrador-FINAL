package co.edu.cesde.ga.domain.repository;

import co.edu.cesde.ga.domain.model.Enrollment;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository {
    Enrollment save(Enrollment enrollment);
    Optional<Enrollment> findById(Long id);
    List<Enrollment> findAll();
    void deleteById(Long id);
    boolean existsById(Long id);
    boolean existsByStudentIdAndGroupIdAndStatus(Long studentId, Long groupId, String status);
}
