package co.edu.cesde.ga.presentation.security;

import co.edu.cesde.ga.application.dto.TokenData;
import co.edu.cesde.ga.application.service.AuthService;
import co.edu.cesde.ga.application.service.TokenService;
import co.edu.cesde.ga.domain.exception.InvalidTokenException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Se ejecuta antes de cada peticion a /api/** (menos login y registro) y exige un token valido
 * de una cuenta que siga activa. Si falla, lanza InvalidTokenException y se responde 401.
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static final String BEARER = "Bearer ";

    private final TokenService tokenService;
    private final AuthService authService;

    public AuthInterceptor(TokenService tokenService, AuthService authService) {
        this.tokenService = tokenService;
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Antes de PUT, PATCH o DELETE el navegador envia una peticion OPTIONS (CORS) sin token: se deja pasar.
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith(BEARER)) {
            throw new InvalidTokenException("Falta el token de autenticacion");
        }

        TokenData data = tokenService.parseToken(header.substring(BEARER.length()).trim());
        if (data.userId() == null) {
            throw new InvalidTokenException("Token invalido o vencido");
        }

        // Si un administrador desactivo la cuenta, el token deja de servir de inmediato.
        authService.verifyActiveUser(data.userId());

        request.setAttribute("authUserId", data.userId());
        request.setAttribute("authUsername", data.username());
        return true;
    }
}
