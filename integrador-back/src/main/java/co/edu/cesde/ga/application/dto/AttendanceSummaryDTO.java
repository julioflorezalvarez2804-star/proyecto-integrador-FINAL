package co.edu.cesde.ga.application.dto;

// Resumen de asistencia de un estudiante. attendanceRate = (presentes + tardes) / total * 100.
public record AttendanceSummaryDTO(
        Long studentId,
        int present,
        int absent,
        int late,
        int excused,
        int total,
        double attendanceRate
) {
}
