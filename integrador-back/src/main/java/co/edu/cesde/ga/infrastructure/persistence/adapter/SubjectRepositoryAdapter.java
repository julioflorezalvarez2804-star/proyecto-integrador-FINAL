package co.edu.cesde.ga.infrastructure.persistence.adapter;

import co.edu.cesde.ga.domain.exception.ProgramNotFoundException;
import co.edu.cesde.ga.domain.model.Subject;
import co.edu.cesde.ga.domain.repository.SubjectRepository;
import co.edu.cesde.ga.infrastructure.persistence.entity.ProgramEntity;
import co.edu.cesde.ga.infrastructure.persistence.entity.SubjectEntity;
import co.edu.cesde.ga.infrastructure.persistence.jpa.ProgramJpaRepository;
import co.edu.cesde.ga.infrastructure.persistence.jpa.SubjectJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class SubjectRepositoryAdapter implements SubjectRepository {

    private final SubjectJpaRepository jpaRepository;
    private final ProgramJpaRepository programJpaRepository;

    public SubjectRepositoryAdapter(SubjectJpaRepository jpaRepository, ProgramJpaRepository programJpaRepository) {
        this.jpaRepository = jpaRepository;
        this.programJpaRepository = programJpaRepository;
    }

    @Override
    public Subject save(Subject subject) {
        SubjectEntity entity = new SubjectEntity();
        entity.setSubjectId(subject.getSubjectId());
        entity.setCode(subject.getCode());
        entity.setName(subject.getName());
        entity.setCredits(subject.getCredits());

        ProgramEntity program = programJpaRepository.findById(subject.getProgramId())
                .orElseThrow(() -> new ProgramNotFoundException(subject.getProgramId()));
        entity.setProgram(program);

        return toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<Subject> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Subject> findAll() {
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

    private Subject toDomain(SubjectEntity entity) {
        return new Subject(
                entity.getSubjectId(),
                entity.getCode(),
                entity.getName(),
                entity.getCredits(),
                entity.getProgram().getProgramId());
    }
}
