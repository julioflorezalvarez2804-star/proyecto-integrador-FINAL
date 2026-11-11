package co.edu.cesde.ga.application.service.impl;

import co.edu.cesde.ga.application.dto.RoleDTO;
import co.edu.cesde.ga.application.service.RoleService;
import co.edu.cesde.ga.domain.exception.RoleAlreadyExistsException;
import co.edu.cesde.ga.domain.exception.RoleNotFoundException;
import co.edu.cesde.ga.domain.model.Role;
import co.edu.cesde.ga.domain.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public RoleDTO create(RoleDTO dto) {
        if (roleRepository.existsByName(dto.getName())) {
            throw new RoleAlreadyExistsException(dto.getName());
        }
        Role role = new Role(null, dto.getName(), dto.getDescription());
        return toDTO(roleRepository.save(role));
    }

    @Override
    public RoleDTO findById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException(id));
        return toDTO(role);
    }

    @Override
    public List<RoleDTO> findAll() {
        return roleRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public RoleDTO update(Long id, RoleDTO dto) {
        Role existing = roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException(id));
        if (!existing.getName().equals(dto.getName()) && roleRepository.existsByName(dto.getName())) {
            throw new RoleAlreadyExistsException(dto.getName());
        }
        Role role = new Role(id, dto.getName(), dto.getDescription());
        return toDTO(roleRepository.save(role));
    }

    @Override
    public void delete(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new RoleNotFoundException(id);
        }
        roleRepository.deleteById(id);
    }

    private RoleDTO toDTO(Role role) {
        return new RoleDTO(role.getRoleId(), role.getName(), role.getDescription());
    }
}
