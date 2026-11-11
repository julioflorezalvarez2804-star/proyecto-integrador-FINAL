package co.edu.cesde.ga.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateGroupDTO(
        @NotBlank(message = "El codigo es obligatorio")
        String code,

        @NotNull(message = "El programa es obligatorio")
        Long programId,

        @NotNull(message = "El periodo es obligatorio")
        Long periodId,

        @NotBlank(message = "La jornada es obligatoria")
        String shift
) {
}
