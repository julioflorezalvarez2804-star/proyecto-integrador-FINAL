package co.edu.cesde.ga.domain.exception;

public class GradeNotFoundException extends ResourceNotFoundException {
    public GradeNotFoundException(Long id) {
        super("Grade not found: " + id);
    }
}
