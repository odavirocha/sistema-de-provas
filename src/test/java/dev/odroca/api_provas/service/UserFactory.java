package dev.odroca.api_provas.service;

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
        ReflectionTestUtils.setField(user, "id", UUID.fromString("d50805e1-777c-47dc-9b85-8583e0b671ef"));
        return user;
    }
}
