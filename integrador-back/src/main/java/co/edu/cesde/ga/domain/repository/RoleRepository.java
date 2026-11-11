package co.edu.cesde.ga.domain.repository;

import co.edu.cesde.ga.domain.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleRepository {
    Role save(Role role);
    Optional<Role> findById(Long id);
    List<Role> findAll();
    void deleteById(Long id);
    boolean existsById(Long id);
    boolean existsByName(String name);
}
