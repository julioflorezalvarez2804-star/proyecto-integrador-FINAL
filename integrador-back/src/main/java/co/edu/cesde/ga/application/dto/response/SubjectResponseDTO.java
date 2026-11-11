package co.edu.cesde.ga.application.dto.response;

import co.edu.cesde.ga.application.dto.SubjectDTO;

public record SubjectResponseDTO(
        Long subjectId,
        String code,
        String name,
        int credits,
        Long programId
) {
    public static SubjectResponseDTO from(SubjectDTO dto) {
        return new SubjectResponseDTO(dto.getSubjectId(), dto.getCode(), dto.getName(), dto.getCredits(), dto.getProgramId());
    }
}
