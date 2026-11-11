package co.edu.cesde.ga.infrastructure.persistence.adapter;

import co.edu.cesde.ga.domain.exception.GroupSubjectNotFoundException;
import co.edu.cesde.ga.domain.exception.StudentNotFoundException;
import co.edu.cesde.ga.domain.model.Attendance;
import co.edu.cesde.ga.domain.repository.AttendanceRepository;
import co.edu.cesde.ga.infrastructure.persistence.entity.AttendanceEntity;
import co.edu.cesde.ga.infrastructure.persistence.entity.GroupSubjectEntity;
import co.edu.cesde.ga.infrastructure.persistence.entity.StudentEntity;
import co.edu.cesde.ga.infrastructure.persistence.jpa.AttendanceJpaRepository;
import co.edu.cesde.ga.infrastructure.persistence.jpa.GroupSubjectJpaRepository;
import co.edu.cesde.ga.infrastructure.persistence.jpa.StudentJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class AttendanceRepositoryAdapter implements AttendanceRepository {

    private final AttendanceJpaRepository jpaRepository;
    private final GroupSubjectJpaRepository groupSubjectJpaRepository;
    private final StudentJpaRepository studentJpaRepository;

    public AttendanceRepositoryAdapter(AttendanceJpaRepository jpaRepository,
                                       GroupSubjectJpaRepository groupSubjectJpaRepository,
                                       StudentJpaRepository studentJpaRepository) {
        this.jpaRepository = jpaRepository;
        this.groupSubjectJpaRepository = groupSubjectJpaRepository;
        this.studentJpaRepository = studentJpaRepository;
    }

    @Override
    public Attendance save(Attendance attendance) {
        AttendanceEntity entity = new AttendanceEntity();
        entity.setAttendanceId(attendance.getAttendanceId());
        entity.setClassDate(attendance.getClassDate());
        entity.setStatus(attendance.getStatus());
        entity.setObservation(attendance.getObservation());

        GroupSubjectEntity groupSubject = groupSubjectJpaRepository.findById(attendance.getGroupSubjectId())
                .orElseThrow(() -> new GroupSubjectNotFoundException(attendance.getGroupSubjectId()));
        StudentEntity student = studentJpaRepository.findById(attendance.getStudentId())
                .orElseThrow(() -> new StudentNotFoundException(attendance.getStudentId()));

        entity.setGroupSubject(groupSubject);
        entity.setStudent(student);

        return toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<Attendance> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Attendance> findAll() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public List<Attendance> findByGroupSubjectId(Long groupSubjectId) {
        return jpaRepository.findByGroupSubject_GroupSubjectId(groupSubjectId).stream().map(this::toDomain).toList();
    }

    @Override
    public List<Attendance> findByGroupSubjectIdAndClassDate(Long groupSubjectId, LocalDate classDate) {
        return jpaRepository.findByGroupSubject_GroupSubjectIdAndClassDate(groupSubjectId, classDate).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Optional<Attendance> findByGroupSubjectIdAndStudentIdAndClassDate(Long groupSubjectId, Long studentId, LocalDate classDate) {
        return jpaRepository.findByGroupSubject_GroupSubjectIdAndStudent_UserIDAndClassDate(groupSubjectId, studentId, classDate)
                .map(this::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    private Attendance toDomain(AttendanceEntity entity) {
        return new Attendance(
                entity.getAttendanceId(),
                entity.getGroupSubject().getGroupSubjectId(),
                entity.getStudent().getUserID(),
                entity.getClassDate(),
                entity.getStatus(),
                entity.getObservation());
    }
}
