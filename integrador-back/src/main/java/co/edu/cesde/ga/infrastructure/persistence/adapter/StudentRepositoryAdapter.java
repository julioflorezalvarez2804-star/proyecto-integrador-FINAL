package co.edu.cesde.ga.infrastructure.persistence.adapter;

import co.edu.cesde.ga.domain.model.Student;
import co.edu.cesde.ga.domain.repository.StudentRepository;
import co.edu.cesde.ga.infrastructure.persistence.entity.StudentEntity;
import co.edu.cesde.ga.infrastructure.persistence.jpa.StudentJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class StudentRepositoryAdapter implements StudentRepository {

    private final StudentJpaRepository jpaRepository;

    public StudentRepositoryAdapter(StudentJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Student save(Student student) {
        StudentEntity entity = toEntity(student);
        StudentEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Student> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Student> findAll() {
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
    public Optional<Student> findByDocumentNumber(String documentNumber) {
        return jpaRepository.findByDocumentNumber(documentNumber).map(this::toDomain);
    }

    private StudentEntity toEntity(Student student) {
        StudentEntity entity = new StudentEntity();
        entity.setUserID(student.getUserID());
        entity.setCode(student.getCode());
        entity.setDocumentNumber(student.getDocumentNumber());
        entity.setFirstName(student.getFirstName());
        entity.setLastName(student.getLastName());
        entity.setStatus(student.getStatus());
        entity.setBirthDate(student.getBirthDate());
        return entity;
    }

    private Student toDomain(StudentEntity entity) {
        return new Student(
                entity.getUserID(),
                entity.getCode(),
                entity.getDocumentNumber(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getStatus(),
                entity.getBirthDate());
    }
}
