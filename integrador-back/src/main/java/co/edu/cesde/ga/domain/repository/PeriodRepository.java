package co.edu.cesde.ga.domain.repository;

import co.edu.cesde.ga.domain.model.Period;

import java.util.List;
import java.util.Optional;

public interface PeriodRepository {
    Period save(Period entity);
    Optional<Period> findById(Long id);
    List<Period> findAll();
    void deleteById(Long id);
    boolean existsById(Long id);
    boolean existsByCode(String code);
}
