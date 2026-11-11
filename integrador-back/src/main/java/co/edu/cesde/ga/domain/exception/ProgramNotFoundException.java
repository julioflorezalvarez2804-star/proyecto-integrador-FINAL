package co.edu.cesde.ga.domain.exception;

public class ProgramNotFoundException extends ResourceNotFoundException {
    public ProgramNotFoundException(Long id) {
        super("Program not found: " + id);
    }
}