package co.edu.cesde.ga.presentation.controller;

import co.edu.cesde.ga.application.dto.response.UserRoleResponseDTO;
import co.edu.cesde.ga.application.service.UserRoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-roles")
public class UserRoleController {

    private final UserRoleService userRoleService;

    public UserRoleController(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @PostMapping
    public ResponseEntity<UserRoleResponseDTO> assign(@RequestParam Long userId, @RequestParam Long roleId) {
        var assigned = userRoleService.assignRole(userId, roleId);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserRoleResponseDTO.from(assigned));
    }

    @DeleteMapping
    public ResponseEntity<Void> unassign(@RequestParam Long userId, @RequestParam Long roleId) {
        userRoleService.unassignRole(userId, roleId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserRoleResponseDTO>> findByUserId(@PathVariable Long userId) {
        var roles = userRoleService.findByUserId(userId).stream()
                .map(UserRoleResponseDTO::from)
                .toList();
        return ResponseEntity.ok(roles);
    }
}
