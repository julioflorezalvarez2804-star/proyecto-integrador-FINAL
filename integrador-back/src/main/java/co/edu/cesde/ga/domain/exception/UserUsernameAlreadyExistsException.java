package co.edu.cesde.ga.domain.exception;

public class UserUsernameAlreadyExistsException extends UserAlreadyExistsException {
    public UserUsernameAlreadyExistsException(String username) {
        super("Ya existe un usuario con el username: " + username);
    }
}
