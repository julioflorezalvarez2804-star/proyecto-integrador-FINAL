package co.edu.cesde.ga.application.service.impl;

import co.edu.cesde.ga.application.dto.ProgramDTO;
import co.edu.cesde.ga.application.service.ProgramService;
import co.edu.cesde.ga.domain.exception.ProgramAlreadyExistsException;
import co.edu.cesde.ga.domain.exception.ProgramNotFoundException;
import co.edu.cesde.ga.domain.model.Program;
import co.edu.cesde.ga.domain.repository.ProgramRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProgramServiceImpl implements ProgramService {

    private final ProgramRepository programRepository;

    public ProgramServiceImpl(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }

    @Override
    public ProgramDTO create(ProgramDTO dto) {
        if (programRepository.existsByCode(dto.getCode())) {
            throw new ProgramAlreadyExistsException(dto.getCode());
        }
        Program program = new Program(null, dto.getCode(), dto.getName());
        return toDTO(programRepository.save(program));
    }

    @Override
    public ProgramDTO findById(Long id) {
        Program program = programRepository.findById(id)
                .orElseThrow(() -> new ProgramNotFoundException(id));
        return toDTO(program);
    }

    @Override
    public List<ProgramDTO> findAll() {
        return programRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public ProgramDTO update(Long id, ProgramDTO dto) {
        if (!programRepository.existsById(id)) {
            throw new ProgramNotFoundException(id);
        }
        Program program = new Program(id, dto.getCode(), dto.getName());
        return toDTO(programRepository.save(program));
    }

    @Override
    public void delete(Long id) {
        if (!programRepository.existsById(id)) {
            throw new ProgramNotFoundException(id);
        }
        programRepository.deleteById(id);
    }

    private ProgramDTO toDTO(Program program) {
        return new ProgramDTO(program.getProgramId(), program.getCode(), program.getName());
    }
}