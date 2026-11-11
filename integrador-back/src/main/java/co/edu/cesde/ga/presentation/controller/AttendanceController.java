package co.edu.cesde.ga.presentation.controller;

import co.edu.cesde.ga.application.dto.AttendanceSummaryDTO;
import co.edu.cesde.ga.application.dto.CreateAttendanceSessionDTO;
import co.edu.cesde.ga.application.dto.response.AttendanceResponseDTO;
import co.edu.cesde.ga.application.service.AttendanceService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendances")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    // Registra (o corrige) la lista de asistencia de una clase completa.
    @PostMapping("/sessions")
    public ResponseEntity<List<AttendanceResponseDTO>> registerSession(@Valid @RequestBody CreateAttendanceSessionDTO dto) {
        var saved = attendanceService.registerSession(dto.groupSubjectId(), dto.classDate(), dto.records()).stream()
                .map(AttendanceResponseDTO::from)
                .toList();
        return ResponseEntity.ok(saved);
    }

    // Asistencia de una materia del grupo; con "date" solo esa clase.
    @GetMapping
    public ResponseEntity<List<AttendanceResponseDTO>> findBySession(
            @RequestParam Long groupSubjectId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        var records = attendanceService.findBySession(groupSubjectId, date).stream()
                .map(AttendanceResponseDTO::from)
                .toList();
        return ResponseEntity.ok(records);
    }

    // Resumen por estudiante; sin groupSubjectId resume todas las materias.
    @GetMapping("/summary")
    public ResponseEntity<List<AttendanceSummaryDTO>> summary(@RequestParam(required = false) Long groupSubjectId) {
        return ResponseEntity.ok(attendanceService.summary(groupSubjectId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        attendanceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
