package dev.odroca.api_provas.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import dev.odroca.api_provas.entity.QuestionEntity;
import jakarta.transaction.Transactional;

public interface QuestionRepository extends JpaRepository<QuestionEntity, UUID>{
    Optional<QuestionEntity> findByIdAndTest_UserId(UUID questionId, UUID userId);
    @Modifying
    @Transactional
    int deleteByIdAndTest_UserId(UUID questionId, UUID userId);
}