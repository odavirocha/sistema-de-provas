package dev.odroca.api_provas.security;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CsrfValidationFilter extends OncePerRequestFilter {
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        
        if (!request.getMethod().equals("GET")) {
            String csrfToken = request.getHeader("CSRF-Token");
            String csrfCookie = null;

            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("csrfCookie")) {
                    csrfCookie = cookie.getValue();
                    break;
                }
            }

            response.setContentType("application/json;charset=UTF-8");

            if (csrfCookie == null) {
                response.getWriter().write("{\"error\":\"CSRF_NOT_FOUND\"}");
                return;
            }

            if (!csrfToken.equals(csrfCookie)) {
                response.setStatus(403);
                response.getWriter().write("{\"error\":\"INVALID_CSRF\"}");
                return;
            }
        }

    }

}
