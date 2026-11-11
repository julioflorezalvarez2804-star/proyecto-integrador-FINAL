package co.edu.cesde.ga.application.service;

import co.edu.cesde.ga.application.dto.TokenData;

import java.util.List;

// Puerto: la aplicacion pide "genera / lee un token" sin saber que se usa JWT.
public interface TokenService {
    String generateToken(Long userId, String username, List<String> roles);
    TokenData parseToken(String token);
    long getExpirationSeconds();
}
