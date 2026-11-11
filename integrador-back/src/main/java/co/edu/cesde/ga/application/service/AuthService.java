package co.edu.cesde.ga.application.service;

import co.edu.cesde.ga.application.dto.AuthDTO;
import co.edu.cesde.ga.application.dto.UserDTO;

public interface AuthService {

    AuthDTO login(String username, String rawPassword);

    // Crea una cuenta INACTIVE con el rol PROFESOR. Un administrador debe activarla.
    UserDTO register(String username, String email, String rawPassword);

    // Lanza InvalidTokenException si la cuenta ya no existe o esta inactiva.
    void verifyActiveUser(Long userId);

    // Consulta los roles actuales en la base de datos (no los del token).
    boolean hasRole(Long userId, String roleName);
}
