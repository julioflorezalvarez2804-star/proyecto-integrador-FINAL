package co.edu.cesde.ga.application.dto.response;

import co.edu.cesde.ga.application.dto.AttendanceDTO;

import java.time.LocalDate;

public record AttendanceResponseDTO(
        Long attendanceId,
        Long groupSubjectId,
        Long studentId,
        LocalDate classDate,
        String status,
        String observation
) {
    public static AttendanceResponseDTO from(AttendanceDTO dto) {
        return new AttendanceResponseDTO(dto.attendanceId(), dto.groupSubjectId(), dto.studentId(),
                dto.classDate(), dto.status(), dto.observation());
    }
}
