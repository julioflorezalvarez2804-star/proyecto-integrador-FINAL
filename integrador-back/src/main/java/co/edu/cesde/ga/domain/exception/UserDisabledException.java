package co.edu.cesde.ga.domain.exception;

public class UserDisabledException extends RuntimeException {
    public UserDisabledException() {
        super("El usuario esta inactivo");
    }
}
