package dev.odroca.api_provas.utils;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletResponse;

@Component
public class CookieUtil {
    
    public void addCookie(HttpServletResponse response, String name, String value, int expireIn) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(expireIn)
            .sameSite("Strict")
            .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }
    
}
