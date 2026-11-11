package co.edu.cesde.ga.application.service.impl;

import co.edu.cesde.ga.application.dto.SubjectDTO;
import co.edu.cesde.ga.application.service.SubjectService;
import co.edu.cesde.ga.domain.exception.ProgramNotFoundException;
import co.edu.cesde.ga.domain.exception.SubjectAlreadyExistsException;
import co.edu.cesde.ga.domain.exception.SubjectNotFoundException;
import co.edu.cesde.ga.domain.model.Subject;
import co.edu.cesde.ga.domain.repository.ProgramRepository;
import co.edu.cesde.ga.domain.repository.SubjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;
    private final ProgramRepository programRepository;

    public SubjectServiceImpl(SubjectRepository subjectRepository, ProgramRepository programRepository) {
        this.subjectRepository = subjectRepository;
        this.programRepository = programRepository;
    }

    @Override
    public SubjectDTO create(SubjectDTO dto) {
        validateProgram(dto);
        if (subjectRepository.existsByCode(dto.getCode())) {
            throw new SubjectAlreadyExistsException(dto.getCode());
        }
        Subject subject = new Subject(null, dto.getCode(), dto.getName(), dto.getCredits(), dto.getProgramId());
        return toDTO(subjectRepository.save(subject));
    }

    @Override
    public SubjectDTO findById(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new SubjectNotFoundException(id));
        return toDTO(subject);
    }

    @Override
    public List<SubjectDTO> findAll() {
        return subjectRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public SubjectDTO update(Long id, SubjectDTO dto) {
        if (!subjectRepository.existsById(id)) {
            throw new SubjectNotFoundException(id);
        }
        validateProgram(dto);
        Subject subject = new Subject(id, dto.getCode(), dto.getName(), dto.getCredits(), dto.getProgramId());
        return toDTO(subjectRepository.save(subject));
    }

    @Override
    public void delete(Long id) {
        if (!subjectRepository.existsById(id)) {
            throw new SubjectNotFoundException(id);
        }
        subjectRepository.deleteById(id);
    }

    private void validateProgram(SubjectDTO dto) {
        if (!programRepository.existsById(dto.getProgramId())) {
            throw new ProgramNotFoundException(dto.getProgramId());
        }
    }

    private SubjectDTO toDTO(Subject subject) {
        return new SubjectDTO(subject.getSubjectId(), subject.getCode(), subject.getName(),
                subject.getCredits(), subject.getProgramId());
    }
}
