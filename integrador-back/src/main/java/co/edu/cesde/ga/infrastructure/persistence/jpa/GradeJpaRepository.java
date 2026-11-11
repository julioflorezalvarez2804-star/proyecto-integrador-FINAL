package co.edu.cesde.ga.infrastructure.persistence.jpa;

import co.edu.cesde.ga.infrastructure.persistence.entity.GradeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GradeJpaRepository extends JpaRepository<GradeEntity, Long> {
    Optional<GradeEntity> findByGroupSubject_GroupSubjectIdAndStudent_UserID(Long groupSubjectId, Long studentId);
}
