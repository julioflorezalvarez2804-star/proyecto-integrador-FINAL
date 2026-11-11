package co.edu.cesde.ga.application.dto;

import java.util.List;

// Lo que se lee de un token valido.
public record TokenData(Long userId, String username, List<String> roles) {
}
