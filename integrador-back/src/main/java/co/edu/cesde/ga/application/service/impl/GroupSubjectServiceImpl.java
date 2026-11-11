package co.edu.cesde.ga.application.service.impl;

import co.edu.cesde.ga.application.dto.GroupSubjectDTO;
import co.edu.cesde.ga.application.service.GroupSubjectService;
import co.edu.cesde.ga.domain.exception.BusinessException;
import co.edu.cesde.ga.domain.exception.GroupNotFoundException;
import co.edu.cesde.ga.domain.exception.GroupSubjectAlreadyExistsException;
import co.edu.cesde.ga.domain.exception.GroupSubjectNotFoundException;
import co.edu.cesde.ga.domain.exception.SubjectNotFoundException;
import co.edu.cesde.ga.domain.exception.TeacherNotFoundException;
import co.edu.cesde.ga.domain.model.Group;
import co.edu.cesde.ga.domain.model.GroupSubject;
import co.edu.cesde.ga.domain.model.Subject;
import co.edu.cesde.ga.domain.repository.GroupRepository;
import co.edu.cesde.ga.domain.repository.GroupSubjectRepository;
import co.edu.cesde.ga.domain.repository.SubjectRepository;
import co.edu.cesde.ga.domain.repository.TeacherRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupSubjectServiceImpl implements GroupSubjectService {

    private final GroupSubjectRepository groupSubjectRepository;
    private final GroupRepository groupRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;

    public GroupSubjectServiceImpl(GroupSubjectRepository groupSubjectRepository, GroupRepository groupRepository,
                                    SubjectRepository subjectRepository, TeacherRepository teacherRepository) {
        this.groupSubjectRepository = groupSubjectRepository;
        this.groupRepository = groupRepository;
        this.subjectRepository = subjectRepository;
        this.teacherRepository = teacherRepository;
    }

    @Override
    public GroupSubjectDTO create(GroupSubjectDTO dto) {
        Group group = groupRepository.findById(dto.getGroupId())
                .orElseThrow(() -> new GroupNotFoundException(dto.getGroupId()));
        Subject subject = subjectRepository.findById(dto.getSubjectId())
                .orElseThrow(() -> new SubjectNotFoundException(dto.getSubjectId()));
        if (!teacherRepository.existsById(dto.getTeacherId())) {
            throw new TeacherNotFoundException(dto.getTeacherId());
        }

        // Regla de negocio: la materia debe pertenecer al mismo programa del grupo
        if (!group.getProgramId().equals(subject.getProgramId())) {
            throw new BusinessException("La materia no pertenece al programa del grupo");
        }

        if (groupSubjectRepository.existsByGroupIdAndSubjectId(dto.getGroupId(), dto.getSubjectId())) {
            throw new GroupSubjectAlreadyExistsException(dto.getGroupId(), dto.getSubjectId());
        }

        GroupSubject groupSubject = new GroupSubject(null, dto.getGroupId(), dto.getSubjectId(), dto.getTeacherId());
        return toDTO(groupSubjectRepository.save(groupSubject));
    }

    @Override
    public GroupSubjectDTO findById(Long id) {
        GroupSubject groupSubject = groupSubjectRepository.findById(id)
                .orElseThrow(() -> new GroupSubjectNotFoundException(id));
        return toDTO(groupSubject);
    }

    @Override
    public List<GroupSubjectDTO> findAll() {
        return groupSubjectRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public void delete(Long id) {
        if (!groupSubjectRepository.existsById(id)) {
            throw new GroupSubjectNotFoundException(id);
        }
        groupSubjectRepository.deleteById(id);
    }

    private GroupSubjectDTO toDTO(GroupSubject groupSubject) {
        return new GroupSubjectDTO(groupSubject.getGroupSubjectId(), groupSubject.getGroupId(),
                groupSubject.getSubjectId(), groupSubject.getTeacherId());
    }
}
