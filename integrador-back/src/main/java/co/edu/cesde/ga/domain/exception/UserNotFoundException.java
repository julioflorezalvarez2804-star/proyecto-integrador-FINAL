package co.edu.cesde.ga.domain.exception;

public class UserNotFoundException extends ResourceNotFoundException {
    public UserNotFoundException(Long id) {
        super("User not found: " + id);
    }
}
