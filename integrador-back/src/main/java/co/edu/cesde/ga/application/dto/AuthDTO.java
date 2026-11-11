package co.edu.cesde.ga.application.dto;

import java.util.List;

// Resultado interno de un inicio de sesion correcto.
public record AuthDTO(String token, long expiresInSeconds, UserDTO user, List<String> roles) {
}
