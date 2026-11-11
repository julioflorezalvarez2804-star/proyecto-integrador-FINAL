package co.edu.cesde.ga.domain.exception;

public class EnrollmentAlreadyExistsException extends ResourceAlreadyExistsException {
    public EnrollmentAlreadyExistsException(Long studentId, Long groupId) {
        super("El estudiante " + studentId + " ya tiene una matricula activa en el grupo " + groupId);
    }
}