package co.edu.cesde.ga.domain.exception;

public class StudentAlreadyExistsException extends ResourceAlreadyExistsException {
    public StudentAlreadyExistsException(String message) {
        super(message);
    }
}