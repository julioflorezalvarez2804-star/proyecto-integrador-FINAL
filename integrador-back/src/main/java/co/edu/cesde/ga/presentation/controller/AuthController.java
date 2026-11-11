package co.edu.cesde.ga.presentation.controller;

import co.edu.cesde.ga.application.dto.CreateUserDTO;
import co.edu.cesde.ga.application.dto.LoginRequestDTO;
import co.edu.cesde.ga.application.dto.response.LoginResponseDTO;
import co.edu.cesde.ga.application.dto.response.UserResponseDTO;
import co.edu.cesde.ga.application.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        var auth = authService.login(dto.username(), dto.password());
        return ResponseEntity.ok(LoginResponseDTO.from(auth));
    }

    // Registro publico: la cuenta queda INACTIVE con rol PROFESOR hasta que un administrador la active.
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody CreateUserDTO dto) {
        var created = authService.register(dto.username(), dto.email(), dto.password());
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponseDTO.from(created));
    }
}
