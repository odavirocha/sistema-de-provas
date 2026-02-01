package dev.odroca.api_provas.dto.users;

import dev.odroca.api_provas.enums.Role;

public record RequestAddRoles(
    Role role
) {}
