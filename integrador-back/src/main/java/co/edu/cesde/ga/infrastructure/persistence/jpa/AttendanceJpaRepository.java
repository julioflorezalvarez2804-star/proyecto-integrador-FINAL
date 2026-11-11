package co.edu.cesde.ga.infrastructure.persistence.jpa;

import co.edu.cesde.ga.infrastructure.persistence.entity.AttendanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceJpaRepository extends JpaRepository<AttendanceEntity, Long> {

    List<AttendanceEntity> findByGroupSubject_GroupSubjectId(Long groupSubjectId);

    List<AttendanceEntity> findByGroupSubject_GroupSubjectIdAndClassDate(Long groupSubjectId, LocalDate classDate);

    Optional<AttendanceEntity> findByGroupSubject_GroupSubjectIdAndStudent_UserIDAndClassDate(
            Long groupSubjectId, Long studentId, LocalDate classDate);
}
