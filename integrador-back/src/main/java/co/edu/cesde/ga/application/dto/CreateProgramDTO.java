package co.edu.cesde.ga.application.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateProgramDTO(
        @NotBlank(message = "El codigo es obligatorio")
        String code,

        @NotBlank(message = "El nombre es obligatorio")
        String name
) {
}