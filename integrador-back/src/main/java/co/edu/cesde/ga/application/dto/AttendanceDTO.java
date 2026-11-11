package co.edu.cesde.ga.application.dto;

import java.time.LocalDate;

public record AttendanceDTO(
        Long attendanceId,
        Long groupSubjectId,
        Long studentId,
        LocalDate classDate,
        String status,
        String observation
) {
}
