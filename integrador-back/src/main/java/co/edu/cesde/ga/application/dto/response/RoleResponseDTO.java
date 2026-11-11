package co.edu.cesde.ga.application.dto.response;

import co.edu.cesde.ga.application.dto.RoleDTO;

public record RoleResponseDTO(
        Long roleId,
        String name,
        String description
) {
    public static RoleResponseDTO from(RoleDTO dto) {
        return new RoleResponseDTO(dto.getRoleId(), dto.getName(), dto.getDescription());
    }
}
