package co.edu.cesde.ga.application.dto.response;

import co.edu.cesde.ga.application.dto.EnrollmentDTO;

import java.time.LocalDateTime;

public record EnrollmentResponseDTO(
        Long enrollmentId,
        Long studentId,
        Long groupId,
        String status,
        LocalDateTime enrolledAt
) {
    public static EnrollmentResponseDTO from(EnrollmentDTO dto) {
        return new EnrollmentResponseDTO(dto.getEnrollmentId(), dto.getStudentId(), dto.getGroupId(),
                dto.getStatus(), dto.getEnrolledAt());
    }
}