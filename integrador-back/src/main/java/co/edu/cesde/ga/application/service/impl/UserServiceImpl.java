package co.edu.cesde.ga.application.service.impl;

import co.edu.cesde.ga.application.dto.UserDTO;
import co.edu.cesde.ga.application.service.UserService;
import co.edu.cesde.ga.domain.exception.UserEmailAlreadyExistsException;
import co.edu.cesde.ga.domain.exception.UserNotFoundException;
import co.edu.cesde.ga.domain.exception.UserUsernameAlreadyExistsException;
import co.edu.cesde.ga.domain.model.User;
import co.edu.cesde.ga.domain.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final String ACTIVE = "ACTIVE";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDTO create(String username, String email, String rawPassword) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UserUsernameAlreadyExistsException(username);
        }
        if (userRepository.existsByEmail(email)) {
            throw new UserEmailAlreadyExistsException(email);
        }

        // La contrasena nunca se guarda en texto plano: se guarda su hash BCrypt.
        String passwordHash = passwordEncoder.encode(rawPassword);
        User user = new User(null, username, email, passwordHash, ACTIVE, LocalDateTime.now());
        return toDTO(userRepository.save(user));
    }

    @Override
    public UserDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return toDTO(user);
    }

    @Override
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public UserDTO update(Long id, String username, String email, String status) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (!existing.getUsername().equals(username) && userRepository.findByUsername(username).isPresent()) {
            throw new UserUsernameAlreadyExistsException(username);
        }
        if (!existing.getEmail().equals(email) && userRepository.existsByEmail(email)) {
            throw new UserEmailAlreadyExistsException(email);
        }

        User user = new User(id, username, email, existing.getPasswordHash(), status, existing.getCreatedAt());
        return toDTO(userRepository.save(user));
    }

    @Override
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
    }

    private UserDTO toDTO(User user) {
        return new UserDTO(user.getUserId(), user.getUsername(), user.getEmail(), user.getPasswordHash(),
                user.getStatus(), user.getCreatedAt());
    }
}
