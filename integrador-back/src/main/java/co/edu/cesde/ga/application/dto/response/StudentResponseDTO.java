package co.edu.cesde.ga.application.dto.response;

import co.edu.cesde.ga.application.dto.StudentDTO;

public record StudentResponseDTO(
        Long userID,
        String code,
        String documentNumber,
        String firstName,
        String lastName,
        Boolean status,
        String birthDate
) {
    public static StudentResponseDTO from(StudentDTO dto) {
        return new StudentResponseDTO(dto.getUserID(), dto.getCode(), dto.getDocumentNumber(),
                dto.getFirstName(), dto.getLastName(), dto.getStatus(), dto.getBirthDate());
    }
}