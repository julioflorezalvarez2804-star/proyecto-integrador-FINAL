package co.edu.cesde.ga.application.service.impl;

import co.edu.cesde.ga.application.dto.AuthDTO;
import co.edu.cesde.ga.application.dto.UserDTO;
import co.edu.cesde.ga.application.service.AuthService;
import co.edu.cesde.ga.application.service.TokenService;
import co.edu.cesde.ga.domain.exception.InvalidCredentialsException;
import co.edu.cesde.ga.domain.exception.InvalidTokenException;
import co.edu.cesde.ga.domain.exception.UserDisabledException;
import co.edu.cesde.ga.domain.exception.UserEmailAlreadyExistsException;
import co.edu.cesde.ga.domain.exception.UserUsernameAlreadyExistsException;
import co.edu.cesde.ga.domain.model.Role;
import co.edu.cesde.ga.domain.model.User;
import co.edu.cesde.ga.domain.repository.RoleRepository;
import co.edu.cesde.ga.domain.repository.UserRepository;
import co.edu.cesde.ga.domain.repository.UserRoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class AuthServiceImpl implements AuthService {

    private static final String ACTIVE = "ACTIVE";
    private static final String INACTIVE = "INACTIVE";
    private static final String TEACHER_ROLE = "PROFESOR";

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository,
                           RoleRepository roleRepository, PasswordEncoder passwordEncoder,
                           TokenService tokenService) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @Override
    public AuthDTO login(String username, String rawPassword) {
        // Mismo mensaje si el usuario no existe o la contrasena no coincide: no se revela cual fallo.
        User user = userRepository.findByUsername(username)
                .orElseThrow(InvalidCredentialsException::new);
        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }
        if (!ACTIVE.equals(user.getStatus())) {
            throw new UserDisabledException();
        }

        List<String> roles = roleNamesOf(user.getUserId());
        String token = tokenService.generateToken(user.getUserId(), user.getUsername(), roles);
        return new AuthDTO(token, tokenService.getExpirationSeconds(), toDTO(user), roles);
    }

    @Override
    @Transactional
    public UserDTO register(String username, String email, String rawPassword) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UserUsernameAlreadyExistsException(username);
        }
        if (userRepository.existsByEmail(email)) {
            throw new UserEmailAlreadyExistsException(email);
        }

        // Nadie elige su rol al registrarse: siempre es PROFESOR y la cuenta queda pendiente.
        Role role = roleRepository.findAll().stream()
                .filter(r -> TEACHER_ROLE.equals(r.getName()))
                .findFirst()
                .orElseGet(() -> roleRepository.save(new Role(null, TEACHER_ROLE, "Profesor")));

        User user = userRepository.save(
                new User(null, username, email, passwordEncoder.encode(rawPassword), INACTIVE, LocalDateTime.now()));
        userRoleRepository.assign(user.getUserId(), role.getRoleId());
        return toDTO(user);
    }

    @Override
    public void verifyActiveUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidTokenException("La cuenta ya no existe"));
        if (!ACTIVE.equals(user.getStatus())) {
            throw new InvalidTokenException("La cuenta esta inactiva");
        }
    }

    @Override
    public boolean hasRole(Long userId, String roleName) {
        return roleNamesOf(userId).contains(roleName);
    }

    private List<String> roleNamesOf(Long userId) {
        return userRoleRepository.findByUserId(userId).stream()
                .map(userRole -> roleRepository.findById(userRole.getRoleId()).map(Role::getName).orElse(null))
                .filter(Objects::nonNull)
                .toList();
    }

    private UserDTO toDTO(User user) {
        return new UserDTO(user.getUserId(), user.getUsername(), user.getEmail(),
                user.getPasswordHash(), user.getStatus(), user.getCreatedAt());
    }
}
