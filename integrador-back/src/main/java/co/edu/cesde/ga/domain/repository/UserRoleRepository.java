package co.edu.cesde.ga.domain.repository;

import co.edu.cesde.ga.domain.model.UserRole;

import java.util.List;

public interface UserRoleRepository {
    UserRole assign(Long userId, Long roleId);
    void unassign(Long userId, Long roleId);
    List<UserRole> findByUserId(Long userId);
    boolean existsByUserIdAndRoleId(Long userId, Long roleId);
}
