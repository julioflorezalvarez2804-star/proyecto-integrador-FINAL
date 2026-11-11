package co.edu.cesde.ga.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateStudentDTO(
        @NotBlank(message = "El codigo es obligatorio")
        String code,

        @NotBlank(message = "El numero de documento es obligatorio")
        String documentNumber,

        @NotBlank(message = "El nombre es obligatorio")
        String firstName,

        @NotBlank(message = "El apellido es obligatorio")
        String lastName,

        @NotNull(message = "El estado es obligatorio")
        Boolean status,

        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "La fecha de nacimiento debe tener formato YYYY-MM-DD")
        String birthDate
) {
}