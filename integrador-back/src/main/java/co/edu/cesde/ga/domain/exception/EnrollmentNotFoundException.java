package co.edu.cesde.ga.domain.exception;

public class EnrollmentNotFoundException extends ResourceNotFoundException {
    public EnrollmentNotFoundException(Long id) {
        super("Enrollment not found: " + id);
    }
}