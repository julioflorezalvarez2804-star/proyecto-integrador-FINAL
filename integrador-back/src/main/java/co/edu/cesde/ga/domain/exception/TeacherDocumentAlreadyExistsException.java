package co.edu.cesde.ga.domain.exception;

public class TeacherDocumentAlreadyExistsException extends ResourceAlreadyExistsException {
    public TeacherDocumentAlreadyExistsException(String documentNumber) {
        super("Ya existe un profesor registrado con el documento: " + documentNumber);
    }
}
