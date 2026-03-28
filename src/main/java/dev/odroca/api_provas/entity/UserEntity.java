package dev.odroca.api_provas.entity;

import java.util.Set;
import java.util.UUID;

import dev.odroca.api_provas.enums.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_table")
@Getter
public class UserEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(unique = true)
    @Setter
    private String email;

    @Column(nullable = false)
    @Setter
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Setter
    private Role role;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    @Setter
    private Set<TestEntity> tests;

    public UserEntity() {}

    public UserEntity(String email, String password, Role role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public UserEntity(String email, String password, Role role, Set<TestEntity> tests) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.tests = tests;
    }
}
