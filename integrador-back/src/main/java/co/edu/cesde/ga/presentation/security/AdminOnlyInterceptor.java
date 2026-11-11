package co.edu.cesde.ga.presentation.security;

import co.edu.cesde.ga.application.service.AuthService;
import co.edu.cesde.ga.domain.exception.ForbiddenException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Corre despues de AuthInterceptor y solo en las rutas de gestion de usuarios, roles y asignaciones:
 * deja pasar unicamente a quien tenga el rol ADMIN en este momento (consulta la base de datos,
 * no el token, asi un cambio de rol se aplica sin esperar a que el token venza).
 */
@Component
public class AdminOnlyInterceptor implements HandlerInterceptor {

    private static final String ADMIN = "ADMIN";

    private final AuthService authService;

    public AdminOnlyInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        Object userId = request.getAttribute("authUserId");
        if (!(userId instanceof Long id) || !authService.hasRole(id, ADMIN)) {
            throw new ForbiddenException("No tienes permisos para realizar esta operacion");
        }
        return true;
    }
}
