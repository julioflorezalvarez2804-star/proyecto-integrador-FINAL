package co.edu.cesde.ga.domain.repository;

import co.edu.cesde.ga.domain.model.Program;

import java.util.List;
import java.util.Optional;

public interface ProgramRepository {
    Program save(Program program);
    Optional<Program> findById(Long id);
    List<Program> findAll();
    void deleteById(Long id);
    boolean existsById(Long id);
    boolean existsByCode(String code);
}