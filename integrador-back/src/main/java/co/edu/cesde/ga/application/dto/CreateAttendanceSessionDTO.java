package co.edu.cesde.ga.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record CreateAttendanceSessionDTO(
        @NotNull(message = "La materia del grupo es obligatoria")
        Long groupSubjectId,

        @NotNull(message = "La fecha de la clase es obligatoria")
        LocalDate classDate,

        @NotEmpty(message = "Debes enviar al menos un estudiante")
        @Valid
        List<AttendanceRecordDTO> records
) {
}
