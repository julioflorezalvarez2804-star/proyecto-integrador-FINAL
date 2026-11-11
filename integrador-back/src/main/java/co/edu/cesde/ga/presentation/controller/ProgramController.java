package co.edu.cesde.ga.presentation.controller;

import co.edu.cesde.ga.application.dto.CreateProgramDTO;
import co.edu.cesde.ga.application.dto.ProgramDTO;
import co.edu.cesde.ga.application.dto.UpdateProgramDTO;
import co.edu.cesde.ga.application.dto.response.ProgramResponseDTO;
import co.edu.cesde.ga.application.service.ProgramService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/programs")
public class ProgramController {

    private final ProgramService programService;

    public ProgramController(ProgramService programService) {
        this.programService = programService;
    }

    @PostMapping
    public ResponseEntity<ProgramResponseDTO> create(@Valid @RequestBody CreateProgramDTO dto) {
        var programDTO = new ProgramDTO(null, dto.code(), dto.name());
        var created = programService.create(programDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProgramResponseDTO.from(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgramResponseDTO> findById(@PathVariable Long id) {
        var program = programService.findById(id);
        return ResponseEntity.ok(ProgramResponseDTO.from(program));
    }

    @GetMapping
    public ResponseEntity<List<ProgramResponseDTO>> findAll() {
        var programs = programService.findAll().stream()
                .map(ProgramResponseDTO::from)
                .toList();
        return ResponseEntity.ok(programs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProgramResponseDTO> update(@PathVariable Long id, @Valid @RequestBody UpdateProgramDTO dto) {
        var programDTO = new ProgramDTO(id, dto.code(), dto.name());
        var updated = programService.update(id, programDTO);
        return ResponseEntity.ok(ProgramResponseDTO.from(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        programService.delete(id);
        return ResponseEntity.noContent().build();
    }
}