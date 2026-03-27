package dev.odroca.api_provas.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
@Table(name = "test_table")
@Getter
public class TestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @Setter
    private UserEntity user;

    @Setter
    private String name;
    
    @OneToMany(mappedBy = "test", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    @Setter
    private Set<QuestionEntity> questions = new HashSet<>();

    public TestEntity() {}

    public TestEntity(UserEntity user, String name, Set<QuestionEntity> questions) {
        this.user = user;
        this.name = name;
        this.questions = questions;
    }

    public TestEntity(String name, Set<QuestionEntity> questions) {
        this.name = name;
        this.questions = questions;
    }
}
