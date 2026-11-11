package co.edu.cesde.ga.domain.exception;

public class SubjectNotFoundException extends ResourceNotFoundException {
    public SubjectNotFoundException(Long id) {
        super("Subject not found: " + id);
    }
}
