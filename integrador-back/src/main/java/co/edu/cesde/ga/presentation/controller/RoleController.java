package co.edu.cesde.ga.presentation.controller;

import co.edu.cesde.ga.application.dto.CreateRoleDTO;
import co.edu.cesde.ga.application.dto.RoleDTO;
import co.edu.cesde.ga.application.dto.UpdateRoleDTO;
import co.edu.cesde.ga.application.dto.response.RoleResponseDTO;
import co.edu.cesde.ga.application.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<RoleResponseDTO> create(@Valid @RequestBody CreateRoleDTO dto) {
        var roleDTO = new RoleDTO(null, dto.name(), dto.description());
        var created = roleService.create(roleDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(RoleResponseDTO.from(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> findById(@PathVariable Long id) {
        var role = roleService.findById(id);
        return ResponseEntity.ok(RoleResponseDTO.from(role));
    }

    @GetMapping
    public ResponseEntity<List<RoleResponseDTO>> findAll() {
        var roles = roleService.findAll().stream()
                .map(RoleResponseDTO::from)
                .toList();
        return ResponseEntity.ok(roles);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> update(@PathVariable Long id, @Valid @RequestBody UpdateRoleDTO dto) {
        var roleDTO = new RoleDTO(id, dto.name(), dto.description());
        var updated = roleService.update(id, roleDTO);
        return ResponseEntity.ok(RoleResponseDTO.from(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
