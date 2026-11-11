package co.edu.cesde.ga.domain.repository;

import co.edu.cesde.ga.domain.model.Attendance;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository {
    Attendance save(Attendance attendance);
    Optional<Attendance> findById(Long id);
    List<Attendance> findAll();
    List<Attendance> findByGroupSubjectId(Long groupSubjectId);
    List<Attendance> findByGroupSubjectIdAndClassDate(Long groupSubjectId, LocalDate classDate);
    Optional<Attendance> findByGroupSubjectIdAndStudentIdAndClassDate(Long groupSubjectId, Long studentId, LocalDate classDate);
    void deleteById(Long id);
    boolean existsById(Long id);
}
