package co.edu.cesde.ga.infrastructure.persistence.jpa;

import co.edu.cesde.ga.infrastructure.persistence.entity.TeacherEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherJpaRepository extends JpaRepository<TeacherEntity, Long> {
    boolean existsByDocumentNumber(String documentNumber);
}
