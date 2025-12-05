package dev.odroca.api_provas.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint{
    
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(401);
        
        if (authException == null || authException.getCause() == null) {
            response.getWriter().write("Token inválido.");
            return;
        }

        Throwable cause = authException.getCause();
        String error = cause.getMessage();

        if (cause.getMessage().contains("expired")) {
            error = "EXPIRED_TOKEN";
        }

        // Fazer os invalid token quando for outra bucha.

        response.getWriter().write(error);
    }

}
