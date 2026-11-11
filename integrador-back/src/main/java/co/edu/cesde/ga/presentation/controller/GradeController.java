package co.edu.cesde.ga.presentation.controller;

import co.edu.cesde.ga.application.dto.CreateGradeDTO;
import co.edu.cesde.ga.application.dto.UpdateGradeDTO;
import co.edu.cesde.ga.application.dto.response.GradeResponseDTO;
import co.edu.cesde.ga.application.service.GradeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grades")
public class GradeController {

    private final GradeService gradeService;

    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @PostMapping
    public ResponseEntity<GradeResponseDTO> create(@Valid @RequestBody CreateGradeDTO dto) {
        var created = gradeService.assignGrade(dto.groupSubjectId(), dto.studentId(), dto.finalScore(), dto.observation());
        return ResponseEntity.status(HttpStatus.CREATED).body(GradeResponseDTO.from(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GradeResponseDTO> findById(@PathVariable Long id) {
        var grade = gradeService.findById(id);
        return ResponseEntity.ok(GradeResponseDTO.from(grade));
    }

    @GetMapping
    public ResponseEntity<List<GradeResponseDTO>> findAll() {
        var grades = gradeService.findAll().stream()
                .map(GradeResponseDTO::from)
                .toList();
        return ResponseEntity.ok(grades);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GradeResponseDTO> update(@PathVariable Long id, @Valid @RequestBody UpdateGradeDTO dto) {
        var updated = gradeService.update(id, dto.finalScore(), dto.observation());
        return ResponseEntity.ok(GradeResponseDTO.from(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        gradeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
