package co.edu.cesde.ga.presentation.controller;

import co.edu.cesde.ga.application.dto.CreateUserDTO;
import co.edu.cesde.ga.application.dto.UpdateUserDTO;
import co.edu.cesde.ga.application.dto.response.UserResponseDTO;
import co.edu.cesde.ga.application.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody CreateUserDTO dto) {
        var created = userService.create(dto.username(), dto.email(), dto.password());
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponseDTO.from(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {
        var user = userService.findById(id);
        return ResponseEntity.ok(UserResponseDTO.from(user));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAll() {
        var users = userService.findAll().stream()
                .map(UserResponseDTO::from)
                .toList();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable Long id, @Valid @RequestBody UpdateUserDTO dto) {
        var updated = userService.update(id, dto.username(), dto.email(), dto.status());
        return ResponseEntity.ok(UserResponseDTO.from(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
