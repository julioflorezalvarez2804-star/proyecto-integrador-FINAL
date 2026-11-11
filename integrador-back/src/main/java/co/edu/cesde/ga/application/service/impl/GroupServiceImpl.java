package co.edu.cesde.ga.application.service.impl;

import co.edu.cesde.ga.application.dto.GroupDTO;
import co.edu.cesde.ga.application.service.GroupService;
import co.edu.cesde.ga.domain.exception.GroupAlreadyExistsException;
import co.edu.cesde.ga.domain.exception.GroupNotFoundException;
import co.edu.cesde.ga.domain.exception.PeriodNotFoundException;
import co.edu.cesde.ga.domain.exception.ProgramNotFoundException;
import co.edu.cesde.ga.domain.model.Group;
import co.edu.cesde.ga.domain.repository.GroupRepository;
import co.edu.cesde.ga.domain.repository.PeriodRepository;
import co.edu.cesde.ga.domain.repository.ProgramRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final ProgramRepository programRepository;
    private final PeriodRepository periodRepository;

    public GroupServiceImpl(GroupRepository groupRepository, ProgramRepository programRepository, PeriodRepository periodRepository) {
        this.groupRepository = groupRepository;
        this.programRepository = programRepository;
        this.periodRepository = periodRepository;
    }

    @Override
    public GroupDTO create(GroupDTO dto) {
        validateReferences(dto);
        if (groupRepository.existsByCode(dto.getCode())) {
            throw new GroupAlreadyExistsException(dto.getCode());
        }
        Group group = new Group(null, dto.getCode(), dto.getProgramId(), dto.getPeriodId(), dto.getShift());
        return toDTO(groupRepository.save(group));
    }

    @Override
    public GroupDTO findById(Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException(id));
        return toDTO(group);
    }

    @Override
    public List<GroupDTO> findAll() {
        return groupRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public GroupDTO update(Long id, GroupDTO dto) {
        if (!groupRepository.existsById(id)) {
            throw new GroupNotFoundException(id);
        }
        validateReferences(dto);
        Group group = new Group(id, dto.getCode(), dto.getProgramId(), dto.getPeriodId(), dto.getShift());
        return toDTO(groupRepository.save(group));
    }

    @Override
    public void delete(Long id) {
        if (!groupRepository.existsById(id)) {
            throw new GroupNotFoundException(id);
        }
        groupRepository.deleteById(id);
    }

    private void validateReferences(GroupDTO dto) {
        if (!programRepository.existsById(dto.getProgramId())) {
            throw new ProgramNotFoundException(dto.getProgramId());
        }
        if (!periodRepository.existsById(dto.getPeriodId())) {
            throw new PeriodNotFoundException(dto.getPeriodId());
        }
    }

    private GroupDTO toDTO(Group group) {
        return new GroupDTO(group.getGroupId(), group.getCode(), group.getProgramId(), group.getPeriodId(), group.getShift());
    }
}
