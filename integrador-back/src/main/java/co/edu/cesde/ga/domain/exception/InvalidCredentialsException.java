package co.edu.cesde.ga.domain.exception;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("Usuario o contrasena incorrectos");
    }
}
