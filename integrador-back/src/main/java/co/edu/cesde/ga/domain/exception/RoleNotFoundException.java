package co.edu.cesde.ga.domain.exception;

public class RoleNotFoundException extends ResourceNotFoundException {
    public RoleNotFoundException(Long id) {
        super("Role not found: " + id);
    }
}
