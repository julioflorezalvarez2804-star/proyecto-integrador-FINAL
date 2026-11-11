package co.edu.cesde.ga.domain.exception;

public class SubjectAlreadyExistsException extends ResourceAlreadyExistsException {
    public SubjectAlreadyExistsException(String code) {
        super("Ya existe una materia con el codigo: " + code);
    }
}
