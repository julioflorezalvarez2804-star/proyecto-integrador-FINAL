package co.edu.cesde.ga.infrastructure.persistence.adapter;

import co.edu.cesde.ga.domain.model.Period;
import co.edu.cesde.ga.domain.repository.PeriodRepository;
import co.edu.cesde.ga.infrastructure.persistence.entity.PeriodEntity;
import co.edu.cesde.ga.infrastructure.persistence.jpa.PeriodJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PeriodRepositoryAdapter implements PeriodRepository {

    private final PeriodJpaRepository jpaRepository;

    public PeriodRepositoryAdapter(PeriodJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Period save(Period period) {
        PeriodEntity entity = toEntity(period);
        return toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<Period> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Period> findAll() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public boolean existsByCode(String code) {
        return jpaRepository.existsByCode(code);
    }

    private PeriodEntity toEntity(Period period) {
        PeriodEntity entity = new PeriodEntity();
        entity.setPeriodId(period.getPeriodId());
        entity.setCode(period.getCode());
        entity.setStartDate(period.getStartDate());
        entity.setEndDate(period.getEndDate());
        return entity;
    }

    private Period toDomain(PeriodEntity entity) {
        return new Period(entity.getPeriodId(), entity.getCode(), entity.getStartDate(), entity.getEndDate());
    }
}
