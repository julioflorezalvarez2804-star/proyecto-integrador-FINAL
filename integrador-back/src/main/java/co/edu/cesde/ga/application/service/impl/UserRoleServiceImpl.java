package co.edu.cesde.ga.application.service.impl;

import co.edu.cesde.ga.application.dto.UserRoleDTO;
import co.edu.cesde.ga.application.service.UserRoleService;
import co.edu.cesde.ga.domain.exception.RoleNotFoundException;
import co.edu.cesde.ga.domain.exception.UserNotFoundException;
import co.edu.cesde.ga.domain.exception.UserRoleAlreadyExistsException;
import co.edu.cesde.ga.domain.exception.UserRoleNotFoundException;
import co.edu.cesde.ga.domain.model.UserRole;
import co.edu.cesde.ga.domain.repository.RoleRepository;
import co.edu.cesde.ga.domain.repository.UserRepository;
import co.edu.cesde.ga.domain.repository.UserRoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserRoleServiceImpl(UserRoleRepository userRoleRepository, UserRepository userRepository, RoleRepository roleRepository) {
        this.userRoleRepository = userRoleRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserRoleDTO assignRole(Long userId, Long roleId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        if (!roleRepository.existsById(roleId)) {
            throw new RoleNotFoundException(roleId);
        }
        if (userRoleRepository.existsByUserIdAndRoleId(userId, roleId)) {
            throw new UserRoleAlreadyExistsException(userId, roleId);
        }
        UserRole userRole = userRoleRepository.assign(userId, roleId);
        return toDTO(userRole);
    }

    @Override
    public void unassignRole(Long userId, Long roleId) {
        if (!userRoleRepository.existsByUserIdAndRoleId(userId, roleId)) {
            throw new UserRoleNotFoundException(userId, roleId);
        }
        userRoleRepository.unassign(userId, roleId);
    }

    @Override
    public List<UserRoleDTO> findByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        return userRoleRepository.findByUserId(userId).stream().map(this::toDTO).toList();
    }

    private UserRoleDTO toDTO(UserRole userRole) {
        return new UserRoleDTO(userRole.getUserId(), userRole.getRoleId());
    }
}
