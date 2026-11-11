package co.edu.cesde.ga.domain.exception;

public class GroupNotFoundException extends ResourceNotFoundException {
    public GroupNotFoundException(Long id) {
        super("Group not found: " + id);
    }
}
