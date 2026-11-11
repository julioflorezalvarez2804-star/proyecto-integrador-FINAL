package co.edu.cesde.ga.infrastructure.persistence.jpa;

import co.edu.cesde.ga.infrastructure.persistence.entity.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupJpaRepository extends JpaRepository<GroupEntity, Long> {
    boolean existsByCode(String code);
}
