package co.edu.cesde.ga.infrastructure.persistence.adapter;

import co.edu.cesde.ga.domain.exception.GroupSubjectNotFoundException;
import co.edu.cesde.ga.domain.exception.StudentNotFoundException;
import co.edu.cesde.ga.domain.model.Grade;
import co.edu.cesde.ga.domain.repository.GradeRepository;
import co.edu.cesde.ga.infrastructure.persistence.entity.GradeEntity;
import co.edu.cesde.ga.infrastructure.persistence.entity.GroupSubjectEntity;
import co.edu.cesde.ga.infrastructure.persistence.entity.StudentEntity;
import co.edu.cesde.ga.infrastructure.persistence.jpa.GradeJpaRepository;
import co.edu.cesde.ga.infrastructure.persistence.jpa.GroupSubjectJpaRepository;
import co.edu.cesde.ga.infrastructure.persistence.jpa.StudentJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class GradeRepositoryAdapter implements GradeRepository {

    private final GradeJpaRepository jpaRepository;
    private final GroupSubjectJpaRepository groupSubjectJpaRepository;
    private final StudentJpaRepository studentJpaRepository;

    public GradeRepositoryAdapter(GradeJpaRepository jpaRepository,
                                   GroupSubjectJpaRepository groupSubjectJpaRepository,
                                   StudentJpaRepository studentJpaRepository) {
        this.jpaRepository = jpaRepository;
        this.groupSubjectJpaRepository = groupSubjectJpaRepository;
        this.studentJpaRepository = studentJpaRepository;
    }

    @Override
    public Grade save(Grade grade) {
        GradeEntity entity = new GradeEntity();
        entity.setGradeId(grade.getGradeId());
        entity.setFinalScore(grade.getFinalScore());
        entity.setObservation(grade.getObservation());

        GroupSubjectEntity groupSubject = groupSubjectJpaRepository.findById(grade.getGroupSubjectId())
                .orElseThrow(() -> new GroupSubjectNotFoundException(grade.getGroupSubjectId()));
        StudentEntity student = studentJpaRepository.findById(grade.getStudentId())
                .orElseThrow(() -> new StudentNotFoundException(grade.getStudentId()));

        entity.setGroupSubject(groupSubject);
        entity.setStudent(student);

        return toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<Grade> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Grade> findAll() {
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
    public Optional<Grade> findByGroupSubjectIdAndStudentId(Long groupSubjectId, Long studentId) {
        return jpaRepository.findByGroupSubject_GroupSubjectIdAndStudent_UserID(groupSubjectId, studentId)
                .map(this::toDomain);
    }

    private Grade toDomain(GradeEntity entity) {
        return new Grade(
                entity.getGradeId(),
                entity.getGroupSubject().getGroupSubjectId(),
                entity.getStudent().getUserID(),
                entity.getFinalScore(),
                entity.getObservation());
    }
}
