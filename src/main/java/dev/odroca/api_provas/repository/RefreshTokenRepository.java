package dev.odroca.api_provas.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.odroca.api_provas.entity.RefreshTokenEntity;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, UUID> {
    public Optional<RefreshTokenEntity> findByUserId(UUID userId);
}
