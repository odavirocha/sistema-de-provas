package dev.odroca.api_provas.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.odroca.api_provas.entity.OptionEntity;

public interface OptionRepository extends JpaRepository<OptionEntity, UUID>{}
