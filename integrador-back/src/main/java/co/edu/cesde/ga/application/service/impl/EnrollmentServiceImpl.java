package co.edu.cesde.ga.application.service.impl;

import co.edu.cesde.ga.application.dto.EnrollmentDTO;
import co.edu.cesde.ga.application.service.EnrollmentService;
import co.edu.cesde.ga.domain.exception.EnrollmentAlreadyExistsException;
import co.edu.cesde.ga.domain.exception.EnrollmentNotFoundException;
import co.edu.cesde.ga.domain.exception.GroupNotFoundException;
import co.edu.cesde.ga.domain.exception.StudentNotFoundException;
import co.edu.cesde.ga.domain.model.Enrollment;
import co.edu.cesde.ga.domain.repository.EnrollmentRepository;
import co.edu.cesde.ga.domain.repository.GroupRepository;
import co.edu.cesde.ga.domain.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    private static final String ACTIVE = "ACTIVE";
    private static final String CANCELLED = "CANCELLED";
    private static final String COMPLETED = "COMPLETED";

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;

    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository, StudentRepository studentRepository, GroupRepository groupRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.groupRepository = groupRepository;
    }

    @Override
    public EnrollmentDTO enroll(Long studentId, Long groupId) {
        if (!studentRepository.existsById(studentId)) {
            throw new StudentNotFoundException(studentId);
        }
        if (!groupRepository.existsById(groupId)) {
            throw new GroupNotFoundException(groupId);
        }
        if (enrollmentRepository.existsByStudentIdAndGroupIdAndStatus(studentId, groupId, ACTIVE)) {
            throw new EnrollmentAlreadyExistsException(studentId, groupId);
        }

        Enrollment enrollment = new Enrollment(null, studentId, groupId, ACTIVE, LocalDateTime.now());
        return toDTO(enrollmentRepository.save(enrollment));
    }

    @Override
    public EnrollmentDTO findById(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new EnrollmentNotFoundException(id));
        return toDTO(enrollment);
    }

    @Override
    public List<EnrollmentDTO> findAll() {
        return enrollmentRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public void cancel(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new EnrollmentNotFoundException(id));
        enrollment.setStatus(CANCELLED);
        enrollmentRepository.save(enrollment);
    }

    @Override
    public void complete(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new EnrollmentNotFoundException(id));
        enrollment.setStatus(COMPLETED);
        enrollmentRepository.save(enrollment);
    }

    @Override
    public void delete(Long id) {
        if (!enrollmentRepository.existsById(id)) {
            throw new EnrollmentNotFoundException(id);
        }
        enrollmentRepository.deleteById(id);
    }

    private EnrollmentDTO toDTO(Enrollment enrollment) {
        return new EnrollmentDTO(enrollment.getEnrollmentId(), enrollment.getStudentId(), enrollment.getGroupId(),
                enrollment.getStatus(), enrollment.getEnrolledAt());
    }
}
