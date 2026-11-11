package co.edu.cesde.ga.domain.exception;

public class UserEmailAlreadyExistsException extends UserAlreadyExistsException {
    public UserEmailAlreadyExistsException(String email) {
        super("Ya existe un usuario registrado con el email: " + email);
    }
}
