package co.edu.cesde.ga.application.dto.response;

import co.edu.cesde.ga.application.dto.ProgramDTO;

public record ProgramResponseDTO(
        Long programId,
        String code,
        String name
) {
    public static ProgramResponseDTO from(ProgramDTO dto) {
        return new ProgramResponseDTO(dto.getProgramId(), dto.getCode(), dto.getName());
    }
}