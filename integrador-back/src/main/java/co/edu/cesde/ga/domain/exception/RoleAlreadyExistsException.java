package co.edu.cesde.ga.domain.exception;

public class RoleAlreadyExistsException extends ResourceAlreadyExistsException {
    public RoleAlreadyExistsException(String name) {
        super("Ya existe un rol con el nombre: " + name);
    }
}
