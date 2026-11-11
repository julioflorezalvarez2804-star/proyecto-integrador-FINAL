package co.edu.cesde.ga.domain.exception;

public class StudentNotFoundException extends ResourceNotFoundException {
  public StudentNotFoundException(Long id) {
    super("Student not found: " + id);
  }
}