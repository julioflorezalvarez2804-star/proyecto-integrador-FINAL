package co.edu.cesde.ga.application.dto.response;

import co.edu.cesde.ga.application.dto.GroupDTO;

public record GroupResponseDTO(
        Long groupId,
        String code,
        Long programId,
        Long periodId,
        String shift
) {
    public static GroupResponseDTO from(GroupDTO dto) {
        return new GroupResponseDTO(dto.getGroupId(), dto.getCode(), dto.getProgramId(), dto.getPeriodId(), dto.getShift());
    }
}
