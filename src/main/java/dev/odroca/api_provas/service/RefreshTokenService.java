package dev.odroca.api_provas.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.odroca.api_provas.entity.RefreshTokenEntity;
import dev.odroca.api_provas.entity.UserEntity;
import dev.odroca.api_provas.repository.RefreshTokenRepository;

@Service
@Transactional
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshRepository;

    public Optional<RefreshTokenEntity> verifyExistRefreshTokenOfUser(UUID userId) {
        return refreshRepository.findByUserId(userId);
    }

    @Transactional
    public RefreshTokenEntity createRefreshToken(UserEntity user, Instant expiryDate) {

        Instant timeNow = Instant.now();
            
        RefreshTokenEntity tokenEntity = new RefreshTokenEntity();
    
        tokenEntity.setUser(user);
        tokenEntity.setExpiryDate(expiryDate);
        tokenEntity.setIssuedAt(timeNow);

        return refreshRepository.save(tokenEntity);
    }

    @Transactional
    public void deleteRefreshToken(UUID token) {
        refreshRepository.deleteById(token);
        refreshRepository.flush();
    }
    
}
