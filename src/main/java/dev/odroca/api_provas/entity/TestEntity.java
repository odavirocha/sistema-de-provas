package dev.odroca.api_provas.entity;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

@Entity
@Table(name = "user_table") // Tabela temporaria
public class TestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID Id;

    private String name;
    private String email;
    private String password;
}
