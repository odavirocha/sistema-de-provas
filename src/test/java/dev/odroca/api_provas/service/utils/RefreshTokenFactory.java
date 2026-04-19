package dev.odroca.api_provas.service.utils;

import dev.odroca.api_provas.entity.RefreshTokenEntity;
import dev.odroca.api_provas.entity.UserEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.UUID;

public class RefreshTokenFactory {

    public static RefreshTokenEntity buildRefreshTokenEntity(UserEntity user) {
        RefreshTokenEntity refreshToken = new RefreshTokenEntity(user, Instant.now().plusSeconds((3600 * 24 * 7)), Instant.now());
        ReflectionTestUtils.setField(refreshToken, "refreshToken", UUID.fromString("397eec71-360d-4626-805c-6640be635672"));

        return refreshToken;
    }

    public static RefreshTokenEntity buildRefreshTokenEntityExpired(UserEntity user) {
        RefreshTokenEntity refreshToken = new RefreshTokenEntity(user, Instant.now().minusSeconds((3600 * 24 * 7)), Instant.now());
        ReflectionTestUtils.setField(refreshToken, "refreshToken", UUID.fromString("397eec71-360d-4626-805c-6640be635672"));

        return refreshToken;
    }

}
