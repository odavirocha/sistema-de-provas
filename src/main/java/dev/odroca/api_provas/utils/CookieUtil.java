package dev.odroca.api_provas.utils;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CookieUtil {

    @Autowired
    private JwtDecoder jwtDecoder;
    
    public void addCookie(HttpServletResponse response, String name, String value, int expireIn) {
        
        ResponseCookie cookie = ResponseCookie.from(name, value)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(expireIn)
            .sameSite("Strict")
            // .domain("")
            .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }
    

    // DELETAR DEPOIS, QUE MUDAR TUDO PARA @AuthenticationPrincipal
    public UUID getUserIdByJWT(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        UUID userId = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    return userId = UUID.fromString(jwtDecoder.decode(cookie.getValue()).getSubject());
                }
            }
        }
        return userId;
    }
}
