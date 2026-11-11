package co.edu.cesde.ga.domain.repository;

import co.edu.cesde.ga.domain.model.Group;

import java.util.List;
import java.util.Optional;

public interface GroupRepository {
    Group save(Group entity);
    Optional<Group> findById(Long id);
    List<Group> findAll();
    void deleteById(Long id);
    boolean existsById(Long id);
    boolean existsByCode(String code);
}
