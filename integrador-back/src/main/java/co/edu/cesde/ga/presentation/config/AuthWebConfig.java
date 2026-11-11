package co.edu.cesde.ga.presentation.config;

import co.edu.cesde.ga.presentation.security.AdminOnlyInterceptor;
import co.edu.cesde.ga.presentation.security.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Va aparte de tu WebConfig (CORS): Spring combina todas las clases WebMvcConfigurer.
@Configuration
public class AuthWebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;
    private final AdminOnlyInterceptor adminOnlyInterceptor;

    public AuthWebConfig(AuthInterceptor authInterceptor, AdminOnlyInterceptor adminOnlyInterceptor) {
        this.authInterceptor = authInterceptor;
        this.adminOnlyInterceptor = adminOnlyInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 1) Todo /api/** exige token, salvo el login y el registro.
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/auth/login", "/api/auth/register");

        // 2) Usuarios, roles y asignaciones de rol: solo administradores (se ejecuta despues del anterior).
        registry.addInterceptor(adminOnlyInterceptor)
                .addPathPatterns("/api/users/**", "/api/roles/**", "/api/user-roles/**");
    }
}
