package co.edu.cesde.ga.infrastructure.persistence.adapter;

import co.edu.cesde.ga.domain.model.Role;
import co.edu.cesde.ga.domain.repository.RoleRepository;
import co.edu.cesde.ga.infrastructure.persistence.entity.RoleEntity;
import co.edu.cesde.ga.infrastructure.persistence.jpa.RoleJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RoleRepositoryAdapter implements RoleRepository {

    private final RoleJpaRepository jpaRepository;

    public RoleRepositoryAdapter(RoleJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Role save(Role role) {
        RoleEntity entity = new RoleEntity();
        entity.setRoleId(role.getRoleId());
        entity.setName(role.getName());
        entity.setDescription(role.getDescription());
        return toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<Role> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Role> findAll() {
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
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }

    private Role toDomain(RoleEntity entity) {
        return new Role(entity.getRoleId(), entity.getName(), entity.getDescription());
    }
}
