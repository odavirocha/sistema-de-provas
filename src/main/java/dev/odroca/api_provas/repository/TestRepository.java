package dev.odroca.api_provas.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.odroca.api_provas.entity.TestEntity;

public interface TestRepository extends JpaRepository<TestEntity, UUID> {
    List<TestEntity> findAllByUserId(UUID userId);
    
    @Query("""
        SELECT test from TestEntity test
        LEFT JOIN FETCH test.questions questions
        LEFT JOIN FETCH questions.options
        WHERE test.id = :id
    """)
    Optional<TestEntity> findByIdWithQuestionsAndOptions(@Param("id") UUID id);
}