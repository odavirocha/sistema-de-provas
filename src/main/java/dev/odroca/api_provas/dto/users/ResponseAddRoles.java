package dev.odroca.api_provas.dto.users;

import java.util.UUID;

public record ResponseAddRoles(
    UUID userId,
    String message
) {}
