package dev.odroca.api_provas.security;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import dev.odroca.api_provas.utils.CookieUtil;
import dev.odroca.api_provas.utils.Hmac;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CsrfValidationFilter extends OncePerRequestFilter {
    
    private CookieUtil cookieUtil;
    private Hmac hmac;

    public CsrfValidationFilter(CookieUtil cookieUtil, Hmac hmac) {
        this.cookieUtil = cookieUtil;
        this.hmac = hmac;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        
        // Gera token
        if (request.getRequestURI().equals("/auth/login") || request.getRequestURI().equals("/auth/signup") || request.getRequestURI().equals("/auth/csrf")) {
            
            SecureRandom random = new SecureRandom();
            byte[] csrfTokenBytes = random.generateSeed(32);
            
            String hexResult = hmac.encode(csrfTokenBytes);

            cookieUtil.addCookie(response, "X-XSRF-TOKEN", hexResult, 3600, true);
            cookieUtil.addCookie(response, "XSRF-TOKEN", Base64.getUrlEncoder().withoutPadding().encodeToString(csrfTokenBytes), 3600, false);

            filterChain.doFilter(request, response);
            return;
        }

        if (request.getMethod().equals("GET") ||
            request.getMethod().equals("HEAD") || 
            request.getMethod().equals("OPTIONS") || 
            request.getMethod().equals("TRACE")) {
            filterChain.doFilter(request, response);
            return;
        }

        String csrfTokenHeader = request.getHeader("X-XSRF-TOKEN");
        String csrfTokenCookieHttp = null;
        Cookie[] cookies = request.getCookies();

        if (cookies == null) throw new AccessDeniedException("Nenhum cookie recebido.");
        
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("X-XSRF-TOKEN")) {
                csrfTokenCookieHttp = cookie.getValue();
                break;
            }
        }

        if (csrfTokenHeader == null) {
            log.warn("Tentativa de acesso sem CSRF. IP: {}, URI: {}, Method: {}", request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
            throw new AccessDeniedException("CSRF Token inválido.");
        }
        
        if (csrfTokenCookieHttp == null) {
            log.warn("CSRF não enviado no cookie");
            throw new AccessDeniedException("CSRF Token inválido.");
        }

        // Validação
        try {
            String tokenOfHeader = hmac.encode(Base64.getUrlDecoder().decode(csrfTokenHeader));
    
            if (MessageDigest.isEqual(tokenOfHeader.getBytes(), csrfTokenCookieHttp.getBytes())) {
                filterChain.doFilter(request, response);
                return;
            }
        } catch (IllegalArgumentException e) {
            log.warn("HEader X-XSRF-TOKEN mal formulado. IP: {}", request.getRemoteAddr());
            throw new AccessDeniedException("CSRF Token inválido.");
        }
        throw new AccessDeniedException("CSRF Token inválido.");

    }

}
