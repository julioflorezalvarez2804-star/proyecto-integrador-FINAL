package co.edu.cesde.ga.domain.exception;

public class PeriodAlreadyExistsException extends ResourceAlreadyExistsException {
    public PeriodAlreadyExistsException(String code) {
        super("Ya existe un periodo con el codigo: " + code);
    }
}
