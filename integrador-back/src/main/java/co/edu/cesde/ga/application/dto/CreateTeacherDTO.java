package co.edu.cesde.ga.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTeacherDTO(
        @NotBlank(message = "El codigo es obligatorio")
        String code,

        @NotBlank(message = "El numero de documento es obligatorio")
        String documentNumber,

        @NotBlank(message = "El nombre es obligatorio")
        String firstName,

        @NotBlank(message = "El apellido es obligatorio")
        String lastName,

        @NotNull(message = "El estado es obligatorio")
        Boolean status
) {
}
