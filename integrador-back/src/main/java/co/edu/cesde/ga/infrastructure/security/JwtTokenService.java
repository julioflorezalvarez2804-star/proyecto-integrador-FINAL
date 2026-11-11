package co.edu.cesde.ga.infrastructure.security;

import co.edu.cesde.ga.application.dto.TokenData;
import co.edu.cesde.ga.application.service.TokenService;
import co.edu.cesde.ga.domain.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenService implements TokenService {

    private final SecretKey key;
    private final long expirationMinutes;

    public JwtTokenService(
            @Value("${app.jwt.secret:cambia-esta-clave-por-una-larga-y-aleatoria-de-minimo-32-caracteres}") String secret,
            @Value("${app.jwt.expiration-minutes:120}") long expirationMinutes) {
        byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
        if (bytes.length < 32) {
            throw new IllegalStateException("app.jwt.secret debe tener al menos 32 caracteres");
        }
        this.key = Keys.hmacShaKeyFor(bytes);
        this.expirationMinutes = expirationMinutes;
    }

    @Override
    public String generateToken(Long userId, String username, List<String> roles) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(username)
                .claim("uid", userId)
                .claim("roles", roles)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(expirationMinutes, ChronoUnit.MINUTES)))
                .signWith(key)
                .compact();
    }

    @Override
    public TokenData parseToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            Number uid = claims.get("uid", Number.class);
            Object rolesClaim = claims.get("roles");
            List<String> roles = rolesClaim instanceof List<?> list
                    ? list.stream().map(String::valueOf).toList()
                    : List.of();

            return new TokenData(uid == null ? null : uid.longValue(), claims.getSubject(), roles);
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException("Token invalido o vencido");
        }
    }

    @Override
    public long getExpirationSeconds() {
        return expirationMinutes * 60;
    }
}
