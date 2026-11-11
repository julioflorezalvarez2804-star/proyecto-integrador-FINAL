package co.edu.cesde.ga.domain.exception;

public class PeriodNotFoundException extends ResourceNotFoundException {
    public PeriodNotFoundException(Long id) {
        super("Period not found: " + id);
    }
}
