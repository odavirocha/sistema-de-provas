package dev.odroca.api_provas.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.odroca.api_provas.entity.TestEntity;

public interface TestRepository extends JpaRepository<TestEntity, UUID> {
    public List<TestEntity> findAllByUserId(UUID userId);
}