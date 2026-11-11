package co.edu.cesde.ga.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateRoleDTO(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 50, message = "El nombre no puede superar 50 caracteres")
        String name,

        @Size(max = 255, message = "La descripcion no puede superar 255 caracteres")
        String description
) {
}
