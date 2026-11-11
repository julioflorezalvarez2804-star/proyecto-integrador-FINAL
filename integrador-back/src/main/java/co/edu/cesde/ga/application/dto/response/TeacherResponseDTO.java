package co.edu.cesde.ga.application.dto.response;

import co.edu.cesde.ga.application.dto.TeacherDTO;

public record TeacherResponseDTO(
        Long userID,
        String code,
        String documentNumber,
        String firstName,
        String lastName,
        Boolean status
) {
    public static TeacherResponseDTO from(TeacherDTO dto) {
        return new TeacherResponseDTO(dto.getUserID(), dto.getCode(), dto.getDocumentNumber(),
                dto.getFirstName(), dto.getLastName(), dto.getStatus());
    }
}
