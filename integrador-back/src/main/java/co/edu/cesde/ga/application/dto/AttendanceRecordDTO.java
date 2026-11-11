package co.edu.cesde.ga.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// Una linea de la lista de asistencia: un estudiante y su estado en esa clase.
public record AttendanceRecordDTO(
        @NotNull(message = "El estudiante es obligatorio")
        Long studentId,

        @NotBlank(message = "El estado es obligatorio")
        @Pattern(regexp = "PRESENT|ABSENT|LATE|EXCUSED", message = "El estado debe ser PRESENT, ABSENT, LATE o EXCUSED")
        String status,

        @Size(max = 255, message = "La observacion no puede superar 255 caracteres")
        String observation
) {
}
