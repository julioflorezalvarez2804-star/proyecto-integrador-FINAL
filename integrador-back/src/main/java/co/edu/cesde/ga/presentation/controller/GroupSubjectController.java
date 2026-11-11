package co.edu.cesde.ga.presentation.controller;

import co.edu.cesde.ga.application.dto.CreateGroupSubjectDTO;
import co.edu.cesde.ga.application.dto.GroupSubjectDTO;
import co.edu.cesde.ga.application.dto.response.GroupSubjectResponseDTO;
import co.edu.cesde.ga.application.service.GroupSubjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/group-subjects")
public class GroupSubjectController {

    private final GroupSubjectService groupSubjectService;

    public GroupSubjectController(GroupSubjectService groupSubjectService) {
        this.groupSubjectService = groupSubjectService;
    }

    @PostMapping
    public ResponseEntity<GroupSubjectResponseDTO> create(@Valid @RequestBody CreateGroupSubjectDTO dto) {
        var groupSubjectDTO = new GroupSubjectDTO(null, dto.groupId(), dto.subjectId(), dto.teacherId());
        var created = groupSubjectService.create(groupSubjectDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(GroupSubjectResponseDTO.from(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupSubjectResponseDTO> findById(@PathVariable Long id) {
        var groupSubject = groupSubjectService.findById(id);
        return ResponseEntity.ok(GroupSubjectResponseDTO.from(groupSubject));
    }

    @GetMapping
    public ResponseEntity<List<GroupSubjectResponseDTO>> findAll() {
        var groupSubjects = groupSubjectService.findAll().stream()
                .map(GroupSubjectResponseDTO::from)
                .toList();
        return ResponseEntity.ok(groupSubjects);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        groupSubjectService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
