package co.edu.cesde.ga.application.service;

import co.edu.cesde.ga.application.dto.GroupSubjectDTO;

import java.util.List;

public interface GroupSubjectService {
    GroupSubjectDTO create(GroupSubjectDTO dto);
    GroupSubjectDTO findById(Long id);
    List<GroupSubjectDTO> findAll();
    void delete(Long id);
}
