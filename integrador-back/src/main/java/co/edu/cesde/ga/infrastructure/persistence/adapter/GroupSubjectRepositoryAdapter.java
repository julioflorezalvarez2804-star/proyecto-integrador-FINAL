package co.edu.cesde.ga.infrastructure.persistence.adapter;

import co.edu.cesde.ga.domain.exception.GroupNotFoundException;
import co.edu.cesde.ga.domain.exception.SubjectNotFoundException;
import co.edu.cesde.ga.domain.exception.TeacherNotFoundException;
import co.edu.cesde.ga.domain.model.GroupSubject;
import co.edu.cesde.ga.domain.repository.GroupSubjectRepository;
import co.edu.cesde.ga.infrastructure.persistence.entity.GroupEntity;
import co.edu.cesde.ga.infrastructure.persistence.entity.GroupSubjectEntity;
import co.edu.cesde.ga.infrastructure.persistence.entity.SubjectEntity;
import co.edu.cesde.ga.infrastructure.persistence.entity.TeacherEntity;
import co.edu.cesde.ga.infrastructure.persistence.jpa.GroupJpaRepository;
import co.edu.cesde.ga.infrastructure.persistence.jpa.GroupSubjectJpaRepository;
import co.edu.cesde.ga.infrastructure.persistence.jpa.SubjectJpaRepository;
import co.edu.cesde.ga.infrastructure.persistence.jpa.TeacherJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class GroupSubjectRepositoryAdapter implements GroupSubjectRepository {

    private final GroupSubjectJpaRepository jpaRepository;
    private final GroupJpaRepository groupJpaRepository;
    private final SubjectJpaRepository subjectJpaRepository;
    private final TeacherJpaRepository teacherJpaRepository;

    public GroupSubjectRepositoryAdapter(GroupSubjectJpaRepository jpaRepository,
                                          GroupJpaRepository groupJpaRepository,
                                          SubjectJpaRepository subjectJpaRepository,
                                          TeacherJpaRepository teacherJpaRepository) {
        this.jpaRepository = jpaRepository;
        this.groupJpaRepository = groupJpaRepository;
        this.subjectJpaRepository = subjectJpaRepository;
        this.teacherJpaRepository = teacherJpaRepository;
    }

    @Override
    public GroupSubject save(GroupSubject groupSubject) {
        GroupSubjectEntity entity = new GroupSubjectEntity();
        entity.setGroupSubjectId(groupSubject.getGroupSubjectId());

        GroupEntity group = groupJpaRepository.findById(groupSubject.getGroupId())
                .orElseThrow(() -> new GroupNotFoundException(groupSubject.getGroupId()));
        SubjectEntity subject = subjectJpaRepository.findById(groupSubject.getSubjectId())
                .orElseThrow(() -> new SubjectNotFoundException(groupSubject.getSubjectId()));
        TeacherEntity teacher = teacherJpaRepository.findById(groupSubject.getTeacherId())
                .orElseThrow(() -> new TeacherNotFoundException(groupSubject.getTeacherId()));

        entity.setGroup(group);
        entity.setSubject(subject);
        entity.setTeacher(teacher);

        return toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<GroupSubject> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<GroupSubject> findAll() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public boolean existsByGroupIdAndSubjectId(Long groupId, Long subjectId) {
        return jpaRepository.existsByGroup_GroupIdAndSubject_SubjectId(groupId, subjectId);
    }

    private GroupSubject toDomain(GroupSubjectEntity entity) {
        return new GroupSubject(
                entity.getGroupSubjectId(),
                entity.getGroup().getGroupId(),
                entity.getSubject().getSubjectId(),
                entity.getTeacher().getUserID());
    }
}
