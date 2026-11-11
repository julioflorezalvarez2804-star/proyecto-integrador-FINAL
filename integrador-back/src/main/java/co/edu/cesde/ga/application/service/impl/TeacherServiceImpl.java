package co.edu.cesde.ga.application.service.impl;

import co.edu.cesde.ga.application.dto.TeacherDTO;
import co.edu.cesde.ga.application.service.TeacherService;
import co.edu.cesde.ga.domain.exception.TeacherDocumentAlreadyExistsException;
import co.edu.cesde.ga.domain.exception.TeacherNotFoundException;
import co.edu.cesde.ga.domain.model.Teacher;
import co.edu.cesde.ga.domain.repository.TeacherRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;

    public TeacherServiceImpl(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Override
    public TeacherDTO create(TeacherDTO dto) {
        if (teacherRepository.existsByDocumentNumber(dto.getDocumentNumber())) {
            throw new TeacherDocumentAlreadyExistsException(dto.getDocumentNumber());
        }
        Teacher teacher = new Teacher(null, dto.getCode(), dto.getDocumentNumber(), dto.getFirstName(),
                dto.getLastName(), dto.getStatus());
        return toDTO(teacherRepository.save(teacher));
    }

    @Override
    public TeacherDTO findById(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new TeacherNotFoundException(id));
        return toDTO(teacher);
    }

    @Override
    public List<TeacherDTO> findAll() {
        return teacherRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public TeacherDTO update(Long id, TeacherDTO dto) {
        if (!teacherRepository.existsById(id)) {
            throw new TeacherNotFoundException(id);
        }
        Teacher teacher = new Teacher(id, dto.getCode(), dto.getDocumentNumber(), dto.getFirstName(),
                dto.getLastName(), dto.getStatus());
        return toDTO(teacherRepository.save(teacher));
    }

    @Override
    public void delete(Long id) {
        if (!teacherRepository.existsById(id)) {
            throw new TeacherNotFoundException(id);
        }
        teacherRepository.deleteById(id);
    }

    private TeacherDTO toDTO(Teacher teacher) {
        return new TeacherDTO(teacher.getUserID(), teacher.getCode(), teacher.getDocumentNumber(),
                teacher.getFirstName(), teacher.getLastName(), teacher.getStatus());
    }
}
