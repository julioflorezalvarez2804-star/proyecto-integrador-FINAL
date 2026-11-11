package co.edu.cesde.ga.application.service;

import co.edu.cesde.ga.application.dto.UserRoleDTO;

import java.util.List;

public interface UserRoleService {
    UserRoleDTO assignRole(Long userId, Long roleId);
    void unassignRole(Long userId, Long roleId);
    List<UserRoleDTO> findByUserId(Long userId);
}
