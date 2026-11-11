package co.edu.cesde.ga.domain.exception;

public class GradeAlreadyExistsException extends ResourceAlreadyExistsException {
    public GradeAlreadyExistsException(Long groupSubjectId, Long studentId) {
        super("El estudiante " + studentId + " ya tiene una nota en la materia de grupo " + groupSubjectId);
    }
}
