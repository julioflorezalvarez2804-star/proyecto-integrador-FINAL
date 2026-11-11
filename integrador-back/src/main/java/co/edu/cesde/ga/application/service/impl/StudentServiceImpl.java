package co.edu.cesde.ga.application.service.impl;

import co.edu.cesde.ga.application.dto.StudentDTO;
import co.edu.cesde.ga.application.service.StudentService;
import co.edu.cesde.ga.domain.exception.StudentDocumentAlreadyExistsException;
import co.edu.cesde.ga.domain.exception.StudentNotFoundException;
import co.edu.cesde.ga.domain.model.Student;
import co.edu.cesde.ga.domain.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public StudentDTO create(StudentDTO dto) {
        if (studentRepository.findByDocumentNumber(dto.getDocumentNumber()).isPresent()) {
            throw new StudentDocumentAlreadyExistsException(dto.getDocumentNumber());
        }
        Student student = new Student(null, dto.getCode(), dto.getDocumentNumber(), dto.getFirstName(),
                dto.getLastName(), dto.getStatus(), dto.getBirthDate());
        return toDTO(studentRepository.save(student));
    }

    @Override
    public StudentDTO findById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        return toDTO(student);
    }

    @Override
    public List<StudentDTO> findAll() {
        return studentRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public StudentDTO update(Long id, StudentDTO dto) {
        if (!studentRepository.existsById(id)) {
            throw new StudentNotFoundException(id);
        }
        Student student = new Student(id, dto.getCode(), dto.getDocumentNumber(), dto.getFirstName(),
                dto.getLastName(), dto.getStatus(), dto.getBirthDate());
        return toDTO(studentRepository.save(student));
    }

    @Override
    public void delete(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new StudentNotFoundException(id);
        }
        studentRepository.deleteById(id);
    }

    private StudentDTO toDTO(Student student) {
        return new StudentDTO(student.getUserID(), student.getCode(), student.getDocumentNumber(),
                student.getFirstName(), student.getLastName(), student.getStatus(), student.getBirthDate());
    }
}