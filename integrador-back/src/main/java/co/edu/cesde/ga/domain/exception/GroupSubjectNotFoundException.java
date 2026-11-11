package co.edu.cesde.ga.domain.exception;

public class GroupSubjectNotFoundException extends ResourceNotFoundException {
    public GroupSubjectNotFoundException(Long id) {
        super("GroupSubject not found: " + id);
    }
}
