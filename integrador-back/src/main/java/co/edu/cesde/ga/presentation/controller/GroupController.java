package co.edu.cesde.ga.presentation.controller;

import co.edu.cesde.ga.application.dto.CreateGroupDTO;
import co.edu.cesde.ga.application.dto.GroupDTO;
import co.edu.cesde.ga.application.dto.UpdateGroupDTO;
import co.edu.cesde.ga.application.dto.response.GroupResponseDTO;
import co.edu.cesde.ga.application.service.GroupService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping
    public ResponseEntity<GroupResponseDTO> create(@Valid @RequestBody CreateGroupDTO dto) {
        var groupDTO = new GroupDTO(null, dto.code(), dto.programId(), dto.periodId(), dto.shift());
        var created = groupService.create(groupDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(GroupResponseDTO.from(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupResponseDTO> findById(@PathVariable Long id) {
        var group = groupService.findById(id);
        return ResponseEntity.ok(GroupResponseDTO.from(group));
    }

    @GetMapping
    public ResponseEntity<List<GroupResponseDTO>> findAll() {
        var groups = groupService.findAll().stream()
                .map(GroupResponseDTO::from)
                .toList();
        return ResponseEntity.ok(groups);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupResponseDTO> update(@PathVariable Long id, @Valid @RequestBody UpdateGroupDTO dto) {
        var groupDTO = new GroupDTO(id, dto.code(), dto.programId(), dto.periodId(), dto.shift());
        var updated = groupService.update(id, groupDTO);
        return ResponseEntity.ok(GroupResponseDTO.from(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        groupService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
