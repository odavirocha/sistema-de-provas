package dev.odroca.api_provas.utils;

import java.io.UnsupportedEncodingException;
import java.nio.file.AccessDeniedException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Hmac {
    
    @Value("${SECRET_HMAC}")
    private String secret;

    private final String algorithm = "HmacSHA256";

    public String encode(byte[] token) throws AccessDeniedException {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes("UTF-8"), algorithm);
        
            Mac Hmac = Mac.getInstance(algorithm);
            Hmac.init(secretKey);
        
            byte[] macData = Hmac.doFinal(token);
            
            return HexFormat.of().formatHex(macData);
        } catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException e) {
            log.debug(e.getMessage());
            throw new AccessDeniedException("Falha ao gerar CSRF token");
        }
    }

}
