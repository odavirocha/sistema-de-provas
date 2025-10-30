package dev.odroca.api_provas.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.odroca.api_provas.entity.UserEntity;
import java.util.Optional;


public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    public Optional<UserEntity> findByEmail(String email);
}
