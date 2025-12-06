package dev.odroca.api_provas.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import dev.odroca.api_provas.entity.TestEntity;
import jakarta.transaction.Transactional;

public interface TestRepository extends JpaRepository<TestEntity, UUID> {
    public List<TestEntity> findAllByUserId(UUID userId);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM TestEntity WHERE id = :testId AND userId = :userId")
    public int deleteByIdAndUserId(UUID testId, UUID userId);

    public Optional<TestEntity> findByIdAndUserId(UUID testId, UUID userId);
}