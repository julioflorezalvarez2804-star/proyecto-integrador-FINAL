package co.edu.cesde.ga.presentation.controller;

import co.edu.cesde.ga.application.dto.response.EnrollmentResponseDTO;
import co.edu.cesde.ga.application.service.EnrollmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping
    public ResponseEntity<EnrollmentResponseDTO> enroll(@RequestParam Long studentId, @RequestParam Long groupId) {
        var created = enrollmentService.enroll(studentId, groupId);
        return ResponseEntity.status(HttpStatus.CREATED).body(EnrollmentResponseDTO.from(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentResponseDTO> findById(@PathVariable Long id) {
        var enrollment = enrollmentService.findById(id);
        return ResponseEntity.ok(EnrollmentResponseDTO.from(enrollment));
    }

    @GetMapping
    public ResponseEntity<List<EnrollmentResponseDTO>> findAll() {
        var enrollments = enrollmentService.findAll().stream()
                .map(EnrollmentResponseDTO::from)
                .toList();
        return ResponseEntity.ok(enrollments);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        enrollmentService.cancel(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<Void> complete(@PathVariable Long id) {
        enrollmentService.complete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        enrollmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}