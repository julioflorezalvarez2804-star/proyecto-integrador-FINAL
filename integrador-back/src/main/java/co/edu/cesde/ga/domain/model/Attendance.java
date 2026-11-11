package co.edu.cesde.ga.domain.model;

import java.time.LocalDate;

/**
 * Asistencia de un estudiante a una clase: una materia de un grupo, en una fecha.
 * Estados posibles: PRESENT, ABSENT, LATE, EXCUSED.
 */
public class Attendance {

    private Long attendanceId;
    private Long groupSubjectId;
    private Long studentId;
    private LocalDate classDate;
    private String status;
    private String observation;

    public Attendance(Long attendanceId, Long groupSubjectId, Long studentId, LocalDate classDate,
                      String status, String observation) {
        this.attendanceId = attendanceId;
        this.groupSubjectId = groupSubjectId;
        this.studentId = studentId;
        this.classDate = classDate;
        this.status = status;
        this.observation = observation;
    }

    public Long getAttendanceId() {
        return attendanceId;
    }

    public Long getGroupSubjectId() {
        return groupSubjectId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public LocalDate getClassDate() {
        return classDate;
    }

    public String getStatus() {
        return status;
    }

    public String getObservation() {
        return observation;
    }
}
