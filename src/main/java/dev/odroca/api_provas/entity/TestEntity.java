package dev.odroca.api_provas.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
@Table(name = "test_table")
@Data
public class TestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(name = "user_id")
    private UUID userId;

    private String name;
    
    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<QuestionEntity> questions = new HashSet<>();

}
