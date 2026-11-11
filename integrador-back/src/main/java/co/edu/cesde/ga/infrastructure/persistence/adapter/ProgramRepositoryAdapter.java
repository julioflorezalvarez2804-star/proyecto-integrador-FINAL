package co.edu.cesde.ga.infrastructure.persistence.adapter;

import co.edu.cesde.ga.domain.model.Program;
import co.edu.cesde.ga.domain.repository.ProgramRepository;
import co.edu.cesde.ga.infrastructure.persistence.entity.ProgramEntity;
import co.edu.cesde.ga.infrastructure.persistence.jpa.ProgramJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProgramRepositoryAdapter implements ProgramRepository {

    private final ProgramJpaRepository jpaRepository;

    public ProgramRepositoryAdapter(ProgramJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Program save(Program program) {
        ProgramEntity entity = toEntity(program);
        return toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<Program> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Program> findAll() {
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

    private ProgramEntity toEntity(Program program) {
        ProgramEntity entity = new ProgramEntity();
        entity.setProgramId(program.getProgramId());
        entity.setCode(program.getCode());
        entity.setName(program.getName());
        return entity;
    }

    private Program toDomain(ProgramEntity entity) {
        return new Program(entity.getProgramId(), entity.getCode(), entity.getName());
    }
}