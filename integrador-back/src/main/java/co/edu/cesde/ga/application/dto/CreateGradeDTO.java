package co.edu.cesde.ga.application.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateGradeDTO(
        @NotNull(message = "La materia del grupo es obligatoria")
        Long groupSubjectId,

        @NotNull(message = "El estudiante es obligatorio")
        Long studentId,

        @NotNull(message = "La nota es obligatoria")
        @DecimalMin(value = "0.0", message = "La nota debe estar entre 0.0 y 5.0")
        @DecimalMax(value = "5.0", message = "La nota debe estar entre 0.0 y 5.0")
        Double finalScore,

        @Size(max = 255, message = "La observacion no puede superar 255 caracteres")
        String observation
) {
}
