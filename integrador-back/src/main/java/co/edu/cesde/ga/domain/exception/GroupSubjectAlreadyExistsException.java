package co.edu.cesde.ga.domain.exception;

public class GroupSubjectAlreadyExistsException extends ResourceAlreadyExistsException {
    public GroupSubjectAlreadyExistsException(Long groupId, Long subjectId) {
        super("La materia " + subjectId + " ya esta asignada al grupo " + groupId);
    }
}
