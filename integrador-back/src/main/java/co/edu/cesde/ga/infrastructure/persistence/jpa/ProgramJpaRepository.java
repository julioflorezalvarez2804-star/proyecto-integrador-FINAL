package co.edu.cesde.ga.infrastructure.persistence.jpa;

import co.edu.cesde.ga.infrastructure.persistence.entity.ProgramEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramJpaRepository extends JpaRepository<ProgramEntity, Long> {
    boolean existsByCode(String code);
}