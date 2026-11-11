package co.edu.cesde.ga.infrastructure.persistence.adapter;

import co.edu.cesde.ga.domain.exception.ResourceNotFoundException;
import co.edu.cesde.ga.domain.model.Enrollment;
import co.edu.cesde.ga.domain.repository.EnrollmentRepository;
import co.edu.cesde.ga.infrastructure.persistence.entity.EnrollmentEntity;
import co.edu.cesde.ga.infrastructure.persistence.entity.GroupEntity;
import co.edu.cesde.ga.infrastructure.persistence.entity.StudentEntity;
import co.edu.cesde.ga.infrastructure.persistence.jpa.EnrollmentJpaRepository;
import co.edu.cesde.ga.infrastructure.persistence.jpa.GroupJpaRepository;
import co.edu.cesde.ga.infrastructure.persistence.jpa.StudentJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class EnrollmentRepositoryAdapter implements EnrollmentRepository {

    private final EnrollmentJpaRepository jpaRepository;
    private final StudentJpaRepository studentJpaRepository;
    private final GroupJpaRepository groupJpaRepository;

    public EnrollmentRepositoryAdapter(EnrollmentJpaRepository jpaRepository,
                                        StudentJpaRepository studentJpaRepository,
                                        GroupJpaRepository groupJpaRepository) {
        this.jpaRepository = jpaRepository;
        this.studentJpaRepository = studentJpaRepository;
        this.groupJpaRepository = groupJpaRepository;
    }

    @Override
    public Enrollment save(Enrollment enrollment) {
        EnrollmentEntity entity = new EnrollmentEntity();
        entity.setEnrollmentId(enrollment.getEnrollmentId());
        entity.setStatus(enrollment.getStatus());
        entity.setEnrolledAt(enrollment.getEnrolledAt());

        StudentEntity student = studentJpaRepository.findById(enrollment.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + enrollment.getStudentId()));
        GroupEntity group = groupJpaRepository.findById(enrollment.getGroupId())
                .orElseThrow(() -> new ResourceNotFoundException("Group not found: " + enrollment.getGroupId()));

        entity.setStudent(student);
        entity.setGroup(group);

        return toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<Enrollment> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Enrollment> findAll() {
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
    public boolean existsByStudentIdAndGroupIdAndStatus(Long studentId, Long groupId, String status) {
        return jpaRepository.existsByStudent_UserIDAndGroup_GroupIdAndStatus(studentId, groupId, status);
    }

    private Enrollment toDomain(EnrollmentEntity entity) {
        return new Enrollment(
                entity.getEnrollmentId(),
                entity.getStudent().getUserID(),
                entity.getGroup().getGroupId(),
                entity.getStatus(),
                entity.getEnrolledAt());
    }
}
