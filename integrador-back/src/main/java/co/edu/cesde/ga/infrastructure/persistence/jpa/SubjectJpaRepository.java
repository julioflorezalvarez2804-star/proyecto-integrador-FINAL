package co.edu.cesde.ga.infrastructure.persistence.jpa;

import co.edu.cesde.ga.infrastructure.persistence.entity.SubjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectJpaRepository extends JpaRepository<SubjectEntity, Long> {
    boolean existsByCode(String code);
}
