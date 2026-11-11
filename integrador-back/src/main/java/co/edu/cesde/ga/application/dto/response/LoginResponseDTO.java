package co.edu.cesde.ga.application.dto.response;

import co.edu.cesde.ga.application.dto.AuthDTO;

import java.util.List;

public record LoginResponseDTO(
        String token,
        String tokenType,
        long expiresInSeconds,
        UserResponseDTO user,
        List<String> roles
) {
    public static LoginResponseDTO from(AuthDTO dto) {
        return new LoginResponseDTO(dto.token(), "Bearer", dto.expiresInSeconds(),
                UserResponseDTO.from(dto.user()), dto.roles());
    }
}
