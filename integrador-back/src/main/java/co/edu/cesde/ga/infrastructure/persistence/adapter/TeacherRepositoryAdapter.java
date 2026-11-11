package co.edu.cesde.ga.infrastructure.persistence.adapter;

import co.edu.cesde.ga.domain.model.Teacher;
import co.edu.cesde.ga.domain.repository.TeacherRepository;
import co.edu.cesde.ga.infrastructure.persistence.entity.TeacherEntity;
import co.edu.cesde.ga.infrastructure.persistence.jpa.TeacherJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TeacherRepositoryAdapter implements TeacherRepository {

    private final TeacherJpaRepository jpaRepository;

    public TeacherRepositoryAdapter(TeacherJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Teacher save(Teacher teacher) {
        TeacherEntity entity = toEntity(teacher);
        return toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<Teacher> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Teacher> findAll() {
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
    public boolean existsByDocumentNumber(String documentNumber) {
        return jpaRepository.existsByDocumentNumber(documentNumber);
    }

    private TeacherEntity toEntity(Teacher teacher) {
        TeacherEntity entity = new TeacherEntity();
        entity.setUserID(teacher.getUserID());
        entity.setCode(teacher.getCode());
        entity.setDocumentNumber(teacher.getDocumentNumber());
        entity.setFirstName(teacher.getFirstName());
        entity.setLastName(teacher.getLastName());
        entity.setStatus(teacher.getStatus());
        return entity;
    }

    private Teacher toDomain(TeacherEntity entity) {
        return new Teacher(
                entity.getUserID(),
                entity.getCode(),
                entity.getDocumentNumber(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getStatus());
    }
}
