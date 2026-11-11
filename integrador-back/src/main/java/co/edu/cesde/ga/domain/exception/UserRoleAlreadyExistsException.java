package co.edu.cesde.ga.domain.exception;

public class UserRoleAlreadyExistsException extends ResourceAlreadyExistsException {
    public UserRoleAlreadyExistsException(Long userId, Long roleId) {
        super("El usuario " + userId + " ya tiene asignado el rol " + roleId);
    }
}
