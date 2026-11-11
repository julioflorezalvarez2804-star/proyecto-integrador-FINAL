package co.edu.cesde.ga.infrastructure.persistence.adapter;

import co.edu.cesde.ga.domain.exception.PeriodNotFoundException;
import co.edu.cesde.ga.domain.exception.ProgramNotFoundException;
import co.edu.cesde.ga.domain.model.Group;
import co.edu.cesde.ga.domain.repository.GroupRepository;
import co.edu.cesde.ga.infrastructure.persistence.entity.GroupEntity;
import co.edu.cesde.ga.infrastructure.persistence.entity.PeriodEntity;
import co.edu.cesde.ga.infrastructure.persistence.entity.ProgramEntity;
import co.edu.cesde.ga.infrastructure.persistence.jpa.GroupJpaRepository;
import co.edu.cesde.ga.infrastructure.persistence.jpa.PeriodJpaRepository;
import co.edu.cesde.ga.infrastructure.persistence.jpa.ProgramJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class GroupRepositoryAdapter implements GroupRepository {

    private final GroupJpaRepository jpaRepository;
    private final ProgramJpaRepository programJpaRepository;
    private final PeriodJpaRepository periodJpaRepository;

    public GroupRepositoryAdapter(GroupJpaRepository jpaRepository,
                                   ProgramJpaRepository programJpaRepository,
                                   PeriodJpaRepository periodJpaRepository) {
        this.jpaRepository = jpaRepository;
        this.programJpaRepository = programJpaRepository;
        this.periodJpaRepository = periodJpaRepository;
    }

    @Override
    public Group save(Group group) {
        GroupEntity entity = new GroupEntity();
        entity.setGroupId(group.getGroupId());
        entity.setCode(group.getCode());
        entity.setShift(group.getShift());

        ProgramEntity program = programJpaRepository.findById(group.getProgramId())
                .orElseThrow(() -> new ProgramNotFoundException(group.getProgramId()));
        PeriodEntity period = periodJpaRepository.findById(group.getPeriodId())
                .orElseThrow(() -> new PeriodNotFoundException(group.getPeriodId()));

        entity.setProgram(program);
        entity.setPeriod(period);

        return toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<Group> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Group> findAll() {
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

    private Group toDomain(GroupEntity entity) {
        return new Group(
                entity.getGroupId(),
                entity.getCode(),
                entity.getProgram().getProgramId(),
                entity.getPeriod().getPeriodId(),
                entity.getShift());
    }
}
