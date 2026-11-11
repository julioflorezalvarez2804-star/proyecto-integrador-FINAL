package co.edu.cesde.ga.infrastructure.config;

import co.edu.cesde.ga.domain.model.Role;
import co.edu.cesde.ga.domain.model.User;
import co.edu.cesde.ga.domain.repository.RoleRepository;
import co.edu.cesde.ga.domain.repository.UserRepository;
import co.edu.cesde.ga.domain.repository.UserRoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Crea un usuario administrador al arrancar, solo si app.admin.password esta configurada
 * y ese usuario aun no existe. Hace falta porque, con la API protegida, ya no se puede
 * crear el primer usuario sin haber iniciado sesion.
 */
@Component
public class AdminSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminSeeder.class);
    private static final String ADMIN_ROLE = "ADMIN";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final String username;
    private final String email;
    private final String password;

    public AdminSeeder(UserRepository userRepository, RoleRepository roleRepository,
                       UserRoleRepository userRoleRepository, PasswordEncoder passwordEncoder,
                       @Value("${app.admin.username:admin}") String username,
                       @Value("${app.admin.email:admin@example.com}") String email,
                       @Value("${app.admin.password:}") String password) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Override
    public void run(String... args) {
        if (password == null || password.isBlank()) {
            log.warn("app.admin.password no esta configurada: no se crea el usuario administrador inicial");
            return;
        }
        if (userRepository.findByUsername(username).isPresent()) {
            return;
        }
        if (userRepository.existsByEmail(email)) {
            log.warn("No se crea el administrador: el email {} ya esta en uso", email);
            return;
        }

        Role role = roleRepository.findAll().stream()
                .filter(r -> ADMIN_ROLE.equals(r.getName()))
                .findFirst()
                .orElseGet(() -> roleRepository.save(new Role(null, ADMIN_ROLE, "Administrador del sistema")));

        User admin = userRepository.save(
                new User(null, username, email, passwordEncoder.encode(password), "ACTIVE", LocalDateTime.now()));
        userRoleRepository.assign(admin.getUserId(), role.getRoleId());
        log.info("Usuario administrador inicial creado: {}", username);
    }
}
