package co.edu.cesde.ga.infrastructure.persistence.adapter;

import co.edu.cesde.ga.domain.exception.RoleNotFoundException;
import co.edu.cesde.ga.domain.exception.UserNotFoundException;
import co.edu.cesde.ga.domain.model.UserRole;
import co.edu.cesde.ga.domain.repository.UserRoleRepository;
import co.edu.cesde.ga.infrastructure.persistence.entity.RoleEntity;
import co.edu.cesde.ga.infrastructure.persistence.entity.UserEntity;
import co.edu.cesde.ga.infrastructure.persistence.jpa.RoleJpaRepository;
import co.edu.cesde.ga.infrastructure.persistence.jpa.UserJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Este adapter no tiene tabla propia: la relacion User <-> Role es un @ManyToMany
 * (UserEntity.roles) y JPA genera la tabla intermedia "user_role".
 */
@Repository
@Transactional
public class UserRoleRepositoryAdapter implements UserRoleRepository {

    private final UserJpaRepository userJpaRepository;
    private final RoleJpaRepository roleJpaRepository;

    public UserRoleRepositoryAdapter(UserJpaRepository userJpaRepository, RoleJpaRepository roleJpaRepository) {
        this.userJpaRepository = userJpaRepository;
        this.roleJpaRepository = roleJpaRepository;
    }

    @Override
    public UserRole assign(Long userId, Long roleId) {
        UserEntity user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        RoleEntity role = roleJpaRepository.findById(roleId)
                .orElseThrow(() -> new RoleNotFoundException(roleId));

        user.getRoles().add(role);
        userJpaRepository.save(user);

        return new UserRole(userId, roleId);
    }

    @Override
    public void unassign(Long userId, Long roleId) {
        UserEntity user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        user.getRoles().removeIf(role -> role.getRoleId().equals(roleId));
        userJpaRepository.save(user);
    }

    @Override
    public List<UserRole> findByUserId(Long userId) {
        UserEntity user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return user.getRoles().stream()
                .map(role -> new UserRole(userId, role.getRoleId()))
                .toList();
    }

    @Override
    public boolean existsByUserIdAndRoleId(Long userId, Long roleId) {
        return userJpaRepository.findById(userId)
                .map(user -> user.getRoles().stream().anyMatch(role -> role.getRoleId().equals(roleId)))
                .orElse(false);
    }
}
