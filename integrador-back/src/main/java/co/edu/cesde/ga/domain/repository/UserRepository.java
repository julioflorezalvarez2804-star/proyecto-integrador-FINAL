package co.edu.cesde.ga.domain.repository;

import co.edu.cesde.ga.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    List<User> findAll();
    void deleteById(Long id);
    boolean existsById(Long id);
    Optional<User> findByUsername(String username);
    boolean existsByEmail(String email);
}
