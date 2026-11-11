package co.edu.cesde.ga.application.service;

import co.edu.cesde.ga.application.dto.GroupDTO;

import java.util.List;

public interface GroupService {
    GroupDTO create(GroupDTO dto);
    GroupDTO findById(Long id);
    List<GroupDTO> findAll();
    GroupDTO update(Long id, GroupDTO dto);
    void delete(Long id);
}
