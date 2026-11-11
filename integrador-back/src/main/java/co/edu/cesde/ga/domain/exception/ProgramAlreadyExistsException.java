package co.edu.cesde.ga.domain.exception;

public class ProgramAlreadyExistsException extends ResourceAlreadyExistsException {
    public ProgramAlreadyExistsException(String code) {
        super("Ya existe un programa con el codigo: " + code);
    }
}