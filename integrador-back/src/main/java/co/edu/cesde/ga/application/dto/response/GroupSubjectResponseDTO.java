package co.edu.cesde.ga.application.dto.response;

import co.edu.cesde.ga.application.dto.GroupSubjectDTO;

public record GroupSubjectResponseDTO(
        Long groupSubjectId,
        Long groupId,
        Long subjectId,
        Long teacherId
) {
    public static GroupSubjectResponseDTO from(GroupSubjectDTO dto) {
        return new GroupSubjectResponseDTO(dto.getGroupSubjectId(), dto.getGroupId(), dto.getSubjectId(), dto.getTeacherId());
    }
}
