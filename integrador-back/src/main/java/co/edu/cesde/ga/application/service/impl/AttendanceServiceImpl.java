package co.edu.cesde.ga.application.service.impl;

import co.edu.cesde.ga.application.dto.AttendanceDTO;
import co.edu.cesde.ga.application.dto.AttendanceRecordDTO;
import co.edu.cesde.ga.application.dto.AttendanceSummaryDTO;
import co.edu.cesde.ga.application.service.AttendanceService;
import co.edu.cesde.ga.domain.exception.AttendanceNotFoundException;
import co.edu.cesde.ga.domain.exception.BusinessException;
import co.edu.cesde.ga.domain.exception.GroupSubjectNotFoundException;
import co.edu.cesde.ga.domain.exception.StudentNotFoundException;
import co.edu.cesde.ga.domain.model.Attendance;
import co.edu.cesde.ga.domain.model.GroupSubject;
import co.edu.cesde.ga.domain.repository.AttendanceRepository;
import co.edu.cesde.ga.domain.repository.EnrollmentRepository;
import co.edu.cesde.ga.domain.repository.GroupSubjectRepository;
import co.edu.cesde.ga.domain.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private static final String ACTIVE = "ACTIVE";
    private static final String PRESENT = "PRESENT";
    private static final String ABSENT = "ABSENT";
    private static final String LATE = "LATE";
    private static final String EXCUSED = "EXCUSED";

    private final AttendanceRepository attendanceRepository;
    private final GroupSubjectRepository groupSubjectRepository;
    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;

    public AttendanceServiceImpl(AttendanceRepository attendanceRepository,
                                 GroupSubjectRepository groupSubjectRepository,
                                 StudentRepository studentRepository,
                                 EnrollmentRepository enrollmentRepository) {
        this.attendanceRepository = attendanceRepository;
        this.groupSubjectRepository = groupSubjectRepository;
        this.studentRepository = studentRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @Override
    @Transactional
    public List<AttendanceDTO> registerSession(Long groupSubjectId, LocalDate classDate, List<AttendanceRecordDTO> records) {
        GroupSubject groupSubject = groupSubjectRepository.findById(groupSubjectId)
                .orElseThrow(() -> new GroupSubjectNotFoundException(groupSubjectId));

        if (classDate.isAfter(LocalDate.now())) {
            throw new BusinessException("La fecha de la clase no puede ser futura");
        }

        Set<Long> seen = new HashSet<>();
        for (AttendanceRecordDTO record : records) {
            if (!seen.add(record.studentId())) {
                throw new BusinessException("El estudiante " + record.studentId() + " aparece repetido en la lista");
            }
        }

        // Primero se valida toda la lista: si algo falla, no se guarda nada.
        for (AttendanceRecordDTO record : records) {
            if (!studentRepository.existsById(record.studentId())) {
                throw new StudentNotFoundException(record.studentId());
            }
            if (!enrollmentRepository.existsByStudentIdAndGroupIdAndStatus(
                    record.studentId(), groupSubject.getGroupId(), ACTIVE)) {
                throw new BusinessException("El estudiante " + record.studentId()
                        + " no tiene matricula activa en el grupo de esta materia");
            }
        }

        List<AttendanceDTO> saved = new ArrayList<>();
        for (AttendanceRecordDTO record : records) {
            // Si ya habia un registro de ese estudiante en esa fecha, se actualiza el mismo.
            Long existingId = attendanceRepository
                    .findByGroupSubjectIdAndStudentIdAndClassDate(groupSubjectId, record.studentId(), classDate)
                    .map(Attendance::getAttendanceId)
                    .orElse(null);

            Attendance attendance = new Attendance(existingId, groupSubjectId, record.studentId(), classDate,
                    record.status(), blankToNull(record.observation()));
            saved.add(toDTO(attendanceRepository.save(attendance)));
        }

        return saved.stream().sorted(Comparator.comparing(AttendanceDTO::studentId)).toList();
    }

    @Override
    public List<AttendanceDTO> findBySession(Long groupSubjectId, LocalDate classDate) {
        ensureGroupSubjectExists(groupSubjectId);

        List<Attendance> records = classDate == null
                ? attendanceRepository.findByGroupSubjectId(groupSubjectId)
                : attendanceRepository.findByGroupSubjectIdAndClassDate(groupSubjectId, classDate);

        return records.stream()
                .map(this::toDTO)
                .sorted(Comparator.comparing(AttendanceDTO::classDate).thenComparing(AttendanceDTO::studentId))
                .toList();
    }

    @Override
    public List<AttendanceSummaryDTO> summary(Long groupSubjectId) {
        List<Attendance> records;
        if (groupSubjectId == null) {
            records = attendanceRepository.findAll();
        } else {
            ensureGroupSubjectExists(groupSubjectId);
            records = attendanceRepository.findByGroupSubjectId(groupSubjectId);
        }

        // Por estudiante: [presentes, ausentes, tardes, excusados]
        Map<Long, int[]> counts = new TreeMap<>();
        for (Attendance record : records) {
            int[] c = counts.computeIfAbsent(record.getStudentId(), id -> new int[4]);
            switch (record.getStatus()) {
                case PRESENT -> c[0]++;
                case ABSENT -> c[1]++;
                case LATE -> c[2]++;
                case EXCUSED -> c[3]++;
                default -> {
                }
            }
        }

        return counts.entrySet().stream()
                .map(entry -> {
                    int[] c = entry.getValue();
                    int total = c[0] + c[1] + c[2] + c[3];
                    // Asistio quien estuvo presente o llego tarde.
                    double rate = total == 0 ? 0.0 : Math.round((c[0] + c[2]) * 1000.0 / total) / 10.0;
                    return new AttendanceSummaryDTO(entry.getKey(), c[0], c[1], c[2], c[3], total, rate);
                })
                .toList();
    }

    @Override
    public void delete(Long id) {
        if (!attendanceRepository.existsById(id)) {
            throw new AttendanceNotFoundException(id);
        }
        attendanceRepository.deleteById(id);
    }

    private void ensureGroupSubjectExists(Long groupSubjectId) {
        if (!groupSubjectRepository.existsById(groupSubjectId)) {
            throw new GroupSubjectNotFoundException(groupSubjectId);
        }
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private AttendanceDTO toDTO(Attendance attendance) {
        return new AttendanceDTO(attendance.getAttendanceId(), attendance.getGroupSubjectId(),
                attendance.getStudentId(), attendance.getClassDate(), attendance.getStatus(),
                attendance.getObservation());
    }
}
