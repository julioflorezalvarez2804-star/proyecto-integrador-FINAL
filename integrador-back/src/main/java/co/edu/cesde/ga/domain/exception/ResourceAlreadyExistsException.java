package co.edu.cesde.ga.domain.exception;

public class ResourceAlreadyExistsException extends ResourceConflictException {
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}