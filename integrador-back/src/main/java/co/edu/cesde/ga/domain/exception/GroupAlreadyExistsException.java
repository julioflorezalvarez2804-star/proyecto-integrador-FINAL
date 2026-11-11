package co.edu.cesde.ga.domain.exception;

public class GroupAlreadyExistsException extends ResourceAlreadyExistsException {
    public GroupAlreadyExistsException(String code) {
        super("Ya existe un grupo con el codigo: " + code);
    }
}
