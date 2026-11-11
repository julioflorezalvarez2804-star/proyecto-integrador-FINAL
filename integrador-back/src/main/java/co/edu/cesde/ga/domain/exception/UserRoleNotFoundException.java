package co.edu.cesde.ga.domain.exception;

public class UserRoleNotFoundException extends ResourceNotFoundException {
    public UserRoleNotFoundException(Long userId, Long roleId) {
        super("El usuario " + userId + " no tiene asignado el rol " + roleId);
    }
}
