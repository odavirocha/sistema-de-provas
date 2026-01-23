package dev.odroca.api_provas.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.odroca.api_provas.entity.QuestionEntity;

public interface QuestionRepository extends JpaRepository<QuestionEntity, UUID>{
    @Query("""
       SELECT question
       FROM QuestionEntity question 
       LEFT JOIN FETCH question.options
       WHERE question.id = :id
    """)
    Optional<QuestionEntity> findByIdWithOptions(@Param("id") UUID id);
}
