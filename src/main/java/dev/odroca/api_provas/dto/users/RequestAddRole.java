package dev.odroca.api_provas.dto.users;

import dev.odroca.api_provas.enums.Role;

public record RequestAddRole(
    Role role
) {}
