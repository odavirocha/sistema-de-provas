package dev.odroca.api_provas.service.utils;

import dev.odroca.api_provas.entity.UserEntity;
import dev.odroca.api_provas.enums.Role;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

public class UserFactory {
    public static UserEntity buildUserEntity() {
        UserEntity user = new UserEntity(
        "example@test.com",
        "$2a$10$AAykMIbXIXhWFmXucp8W/.Syvb9oejO4AiuF8QysFRmBxfxKSqeji",
        Role.USER
        );
        ReflectionTestUtils.setField(user, "id", UUID.fromString("397eec71-360d-4626-805c-6640be635690"));
        return user;
    }
}
