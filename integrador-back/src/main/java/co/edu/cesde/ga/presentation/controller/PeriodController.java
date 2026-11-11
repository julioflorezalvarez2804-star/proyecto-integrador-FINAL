package co.edu.cesde.ga.presentation.controller;

import co.edu.cesde.ga.application.dto.CreatePeriodDTO;
import co.edu.cesde.ga.application.dto.PeriodDTO;
import co.edu.cesde.ga.application.dto.UpdatePeriodDTO;
import co.edu.cesde.ga.application.dto.response.PeriodResponseDTO;
import co.edu.cesde.ga.application.service.PeriodService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/periods")
public class PeriodController {

    private final PeriodService periodService;

    public PeriodController(PeriodService periodService) {
        this.periodService = periodService;
    }

    @PostMapping
    public ResponseEntity<PeriodResponseDTO> create(@Valid @RequestBody CreatePeriodDTO dto) {
        var periodDTO = new PeriodDTO(null, dto.code(), dto.startDate(), dto.endDate());
        var created = periodService.create(periodDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(PeriodResponseDTO.from(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PeriodResponseDTO> findById(@PathVariable Long id) {
        var period = periodService.findById(id);
        return ResponseEntity.ok(PeriodResponseDTO.from(period));
    }

    @GetMapping
    public ResponseEntity<List<PeriodResponseDTO>> findAll() {
        var periods = periodService.findAll().stream()
                .map(PeriodResponseDTO::from)
                .toList();
        return ResponseEntity.ok(periods);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PeriodResponseDTO> update(@PathVariable Long id, @Valid @RequestBody UpdatePeriodDTO dto) {
        var periodDTO = new PeriodDTO(id, dto.code(), dto.startDate(), dto.endDate());
        var updated = periodService.update(id, periodDTO);
        return ResponseEntity.ok(PeriodResponseDTO.from(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        periodService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
