package dev.odroca.api_provas.security;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.util.HexFormat;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CsrfValidationFilter extends OncePerRequestFilter {
    
    @Value("${SALTHMAC}")
    private String salt;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        
        if (request.getRequestURI().equals("/auth/login") || request.getRequestURI().equals("/auth/signup")) {
            
            String normalCsrfToken = UUID.randomUUID().toString();
            String algorithm = "HmacSHA256";
            
            try {
                SecretKeySpec secretKey = new SecretKeySpec(salt.getBytes("UTF-8"), algorithm);

                Mac Hmac = Mac.getInstance(algorithm);
                Hmac.init(secretKey);

                byte[] macData = Hmac.doFinal(normalCsrfToken.getBytes("UTF-8"));
                
                String hexResult = HexFormat.of().formatHex(macData);
            } catch (NoSuchAlgorithmException e) {
                
            } catch (InvalidKeyException e) {
                
            }

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
        String csrfTokenCookie = null;
        Cookie[] cookies = request.getCookies();

        if (cookies == null) throw new AccessDeniedException("Nenhum cookie recebido.");
        
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("XSRF-TOKEN")) {
                csrfTokenCookie = cookie.getValue();
                break;
            }
        }

        if (csrfTokenHeader == null) {
            log.debug("CSRF não enviado no header");
            throw new AccessDeniedException("CSRF Token inválido.");
        }
        
        if (csrfTokenCookie == null) {
            log.debug("CSRF não enviado no cookie");
            throw new AccessDeniedException("CSRF Token inválido.");
        }

        if (csrfTokenHeader.equals(csrfTokenCookie)) {
            filterChain.doFilter(request, response);
            return;
        } else {
            throw new AccessDeniedException("CSRF Token inválido.");
        }


    }

}
