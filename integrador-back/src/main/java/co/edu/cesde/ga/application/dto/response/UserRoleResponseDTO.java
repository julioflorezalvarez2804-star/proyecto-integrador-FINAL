package co.edu.cesde.ga.application.dto.response;

import co.edu.cesde.ga.application.dto.UserRoleDTO;

public record UserRoleResponseDTO(
        Long userId,
        Long roleId
) {
    public static UserRoleResponseDTO from(UserRoleDTO dto) {
        return new UserRoleResponseDTO(dto.getUserId(), dto.getRoleId());
    }
}
