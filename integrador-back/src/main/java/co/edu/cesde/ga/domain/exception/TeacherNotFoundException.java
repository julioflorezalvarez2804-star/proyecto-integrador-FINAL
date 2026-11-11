package co.edu.cesde.ga.domain.exception;

public class TeacherNotFoundException extends ResourceNotFoundException {
    public TeacherNotFoundException(Long id) {
        super("Teacher not found: " + id);
    }
}
