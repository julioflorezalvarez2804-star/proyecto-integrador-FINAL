package co.edu.cesde.ga.application.dto.response;

import co.edu.cesde.ga.application.dto.GradeDTO;

public record GradeResponseDTO(
        Long gradeId,
        Long groupSubjectId,
        Long studentId,
        double finalScore,
        String observation
) {
    public static GradeResponseDTO from(GradeDTO dto) {
        return new GradeResponseDTO(dto.getGradeId(), dto.getGroupSubjectId(), dto.getStudentId(),
                dto.getFinalScore(), dto.getObservation());
    }
}
