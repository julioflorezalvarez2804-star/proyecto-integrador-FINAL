package co.edu.cesde.ga.domain.exception;

public class AttendanceNotFoundException extends ResourceNotFoundException {
    public AttendanceNotFoundException(Long id) {
        super("Attendance not found: " + id);
    }
}
