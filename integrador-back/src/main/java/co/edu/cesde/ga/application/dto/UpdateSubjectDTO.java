package co.edu.cesde.ga.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateSubjectDTO(
        @NotBlank(message = "El codigo es obligatorio")
        String code,

        @NotBlank(message = "El nombre es obligatorio")
        String name,

        @NotNull(message = "Los creditos son obligatorios")
        @Min(value = 1, message = "Los creditos deben ser al menos 1")
        Integer credits,

        @NotNull(message = "El programa es obligatorio")
        Long programId
) {
}
