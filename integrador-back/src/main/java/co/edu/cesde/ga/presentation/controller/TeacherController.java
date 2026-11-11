package co.edu.cesde.ga.presentation.controller;

import co.edu.cesde.ga.application.dto.CreateTeacherDTO;
import co.edu.cesde.ga.application.dto.TeacherDTO;
import co.edu.cesde.ga.application.dto.UpdateTeacherDTO;
import co.edu.cesde.ga.application.dto.response.TeacherResponseDTO;
import co.edu.cesde.ga.application.service.TeacherService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @PostMapping
    public ResponseEntity<TeacherResponseDTO> create(@Valid @RequestBody CreateTeacherDTO dto) {
        var teacherDTO = new TeacherDTO(null, dto.code(), dto.documentNumber(), dto.firstName(),
                dto.lastName(), dto.status());
        var created = teacherService.create(teacherDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(TeacherResponseDTO.from(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherResponseDTO> findById(@PathVariable Long id) {
        var teacher = teacherService.findById(id);
        return ResponseEntity.ok(TeacherResponseDTO.from(teacher));
    }

    @GetMapping
    public ResponseEntity<List<TeacherResponseDTO>> findAll() {
        var teachers = teacherService.findAll().stream()
                .map(TeacherResponseDTO::from)
                .toList();
        return ResponseEntity.ok(teachers);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeacherResponseDTO> update(@PathVariable Long id, @Valid @RequestBody UpdateTeacherDTO dto) {
        var teacherDTO = new TeacherDTO(id, dto.code(), dto.documentNumber(), dto.firstName(),
                dto.lastName(), dto.status());
        var updated = teacherService.update(id, teacherDTO);
        return ResponseEntity.ok(TeacherResponseDTO.from(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        teacherService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
