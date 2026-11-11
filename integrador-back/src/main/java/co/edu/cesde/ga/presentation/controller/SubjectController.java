package co.edu.cesde.ga.presentation.controller;

import co.edu.cesde.ga.application.dto.CreateSubjectDTO;
import co.edu.cesde.ga.application.dto.SubjectDTO;
import co.edu.cesde.ga.application.dto.UpdateSubjectDTO;
import co.edu.cesde.ga.application.dto.response.SubjectResponseDTO;
import co.edu.cesde.ga.application.service.SubjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @PostMapping
    public ResponseEntity<SubjectResponseDTO> create(@Valid @RequestBody CreateSubjectDTO dto) {
        var subjectDTO = new SubjectDTO(null, dto.code(), dto.name(), dto.credits(), dto.programId());
        var created = subjectService.create(subjectDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(SubjectResponseDTO.from(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectResponseDTO> findById(@PathVariable Long id) {
        var subject = subjectService.findById(id);
        return ResponseEntity.ok(SubjectResponseDTO.from(subject));
    }

    @GetMapping
    public ResponseEntity<List<SubjectResponseDTO>> findAll() {
        var subjects = subjectService.findAll().stream()
                .map(SubjectResponseDTO::from)
                .toList();
        return ResponseEntity.ok(subjects);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubjectResponseDTO> update(@PathVariable Long id, @Valid @RequestBody UpdateSubjectDTO dto) {
        var subjectDTO = new SubjectDTO(id, dto.code(), dto.name(), dto.credits(), dto.programId());
        var updated = subjectService.update(id, subjectDTO);
        return ResponseEntity.ok(SubjectResponseDTO.from(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        subjectService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
