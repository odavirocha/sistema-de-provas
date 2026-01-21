package dev.odroca.api_provas.entity;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Entity
@Table(name = "refresh_token_table")
@Data
public class RefreshTokenEntity {

    @Id
    @Column(name = "refresh_token", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(value = AccessLevel.NONE)
    private UUID refreshToken;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;
    
    @Column(name = "expiry_date", nullable = false)
    Instant expiryDate;
    
    @Column(name = "issued_at", nullable = false)
    Instant issuedAt;
    
}
