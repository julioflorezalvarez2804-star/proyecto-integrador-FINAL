package co.edu.cesde.ga.domain.exception;

public class StudentDocumentAlreadyExistsException extends StudentAlreadyExistsException {
    public StudentDocumentAlreadyExistsException(String documentNumber) {
        super("Ya existe un estudiante registrado con el documento: " + documentNumber);
    }
}