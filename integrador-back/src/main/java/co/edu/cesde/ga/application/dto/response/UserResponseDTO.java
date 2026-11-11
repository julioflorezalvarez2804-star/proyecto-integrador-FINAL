package co.edu.cesde.ga.application.dto.response;

import co.edu.cesde.ga.application.dto.UserDTO;

import java.time.LocalDateTime;

// Nunca incluye la contrasena ni su hash.
public record UserResponseDTO(
        Long userId,
        String username,
        String email,
        String status,
        LocalDateTime createdAt
) {
    public static UserResponseDTO from(UserDTO dto) {
        return new UserResponseDTO(dto.getUserId(), dto.getUsername(), dto.getEmail(),
                dto.getStatus(), dto.getCreatedAt());
    }
}
