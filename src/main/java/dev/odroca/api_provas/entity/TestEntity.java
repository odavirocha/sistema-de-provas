package dev.odroca.api_provas.entity;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

@Entity
@Table(name = "test_table") // Tabela temporaria
public class TestEntity {

    @Id
    private UUID Id;
    private String name;
    
}
