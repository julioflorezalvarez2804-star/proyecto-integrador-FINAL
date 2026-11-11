package co.edu.cesde.ga.presentation.controller;

import co.edu.cesde.ga.application.dto.CreateStudentDTO;
import co.edu.cesde.ga.application.dto.StudentDTO;
import co.edu.cesde.ga.application.dto.UpdateStudentDTO;
import co.edu.cesde.ga.application.dto.response.StudentResponseDTO;
import co.edu.cesde.ga.application.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<StudentResponseDTO> create(@Valid @RequestBody CreateStudentDTO dto) {
        var studentDTO = new StudentDTO(null, dto.code(), dto.documentNumber(), dto.firstName(),
                dto.lastName(), dto.status(), dto.birthDate());
        var created = studentService.create(studentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(StudentResponseDTO.from(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> findById(@PathVariable Long id) {
        var student = studentService.findById(id);
        return ResponseEntity.ok(StudentResponseDTO.from(student));
    }

    @GetMapping
    public ResponseEntity<List<StudentResponseDTO>> findAll() {
        var students = studentService.findAll().stream()
                .map(StudentResponseDTO::from)
                .toList();
        return ResponseEntity.ok(students);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> update(@PathVariable Long id, @Valid @RequestBody UpdateStudentDTO dto) {
        var studentDTO = new StudentDTO(id, dto.code(), dto.documentNumber(), dto.firstName(),
                dto.lastName(), dto.status(), dto.birthDate());
        var updated = studentService.update(id, studentDTO);
        return ResponseEntity.ok(StudentResponseDTO.from(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        studentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}