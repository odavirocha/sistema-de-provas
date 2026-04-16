package dev.odroca.api_provas.dto.users;

import java.util.UUID;

public record ResponseAddRole(
    UUID userId,
    String message
) {}
