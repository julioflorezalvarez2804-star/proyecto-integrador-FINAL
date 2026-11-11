package co.edu.cesde.ga.application.service;

import co.edu.cesde.ga.application.dto.RoleDTO;

import java.util.List;

public interface RoleService {
    RoleDTO create(RoleDTO dto);
    RoleDTO findById(Long id);
    List<RoleDTO> findAll();
    RoleDTO update(Long id, RoleDTO dto);
    void delete(Long id);
}
