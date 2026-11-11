package co.edu.cesde.ga.application.dto;

import jakarta.validation.constraints.NotNull;

public record CreateGroupSubjectDTO(
        @NotNull(message = "El grupo es obligatorio")
        Long groupId,

        @NotNull(message = "La materia es obligatoria")
        Long subjectId,

        @NotNull(message = "El profesor es obligatorio")
        Long teacherId
) {
}
