package dev.odroca.api_provas.service.refresh;

import dev.odroca.api_provas.entity.RefreshTokenEntity;
import dev.odroca.api_provas.entity.UserEntity;
import dev.odroca.api_provas.exception.InvalidTokenException;
import dev.odroca.api_provas.exception.UnauthorizedException;
import dev.odroca.api_provas.repository.RefreshTokenRepository;
import dev.odroca.api_provas.service.RefreshTokenService;
import dev.odroca.api_provas.service.utils.RefreshTokenFactory;
import dev.odroca.api_provas.service.utils.UserFactory;
import dev.odroca.api_provas.utils.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RefreshTokenServiceTest {

    @InjectMocks
    RefreshTokenService refreshService;

    @Mock
    RefreshTokenRepository refreshRepository;
    @Mock
    JwtEncoder jwt;
    @Mock
    CookieUtil cookie;

    @Test
    @DisplayName("Deve retornar sucesso ao gerar um novo par tokens")
    void refreshSuccessTest() {
        UserEntity user = UserFactory.buildUserEntity();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        Cookie refreshToken = new Cookie("refreshToken", "1c465b2a-f088-48a6-a20f-cb54b3d4f571");
        request.setCookies(refreshToken);

        RefreshTokenEntity refreshTokenEntity = RefreshTokenFactory.buildRefreshTokenEntity(user);
        Jwt jwtMocked = mock(Jwt.class);
        String fakeAccessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.KMUFsIDTnFmyG3nMiGM6H9FNFUROf3wh7SmqJp-QV30";

        when(jwtMocked.getTokenValue()).thenReturn(fakeAccessToken);
        when(jwt.encode(any())).thenReturn(jwtMocked);
        when(refreshRepository.save(any())).thenReturn(RefreshTokenFactory.buildRefreshTokenEntity(user)); // retorno do createRefreshToken
        when(refreshRepository.findByRefreshToken(UUID.fromString(refreshToken.getValue()))).thenReturn(Optional.of(refreshTokenEntity));

        refreshService.refresh(request, response);

        verify(cookie, times(2)).addCookie(any(HttpServletResponse.class), any(String.class), any(String.class), any(int.class), any(Boolean.class));
    }

    @Test
    @DisplayName("Deve retornar UnauthorizedException quando cookies for nulo")
    void refreshUnauthorizedExceptionNoCookiesTest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertThrows(UnauthorizedException.class,() -> refreshService.refresh(request, response));
    }

    @Test
    @DisplayName("Deve retornar UnauthorizedException quando refresh token não for enviado")
    void refreshUnauthorizedExceptionWithoutRefreshTokenTest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        Cookie randomToken = new Cookie("randomToken", "1c465b2a-f088-48a6-a20f-cb54b3d4f571");
        request.setCookies(randomToken);

        assertThrows(UnauthorizedException.class, () -> refreshService.refresh(request, response));
    }


    @Test
    @DisplayName("Deve retornar UnauthorizedException quando refresh token do cookie for inválido")
    void refreshUnauthorizedExceptionInvalidRefreshTokenTest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        Cookie refreshToken = new Cookie("refreshToken", "1c465b2a-f088-48a6-a20f-cb54b3d4f5");
        request.setCookies(refreshToken);

        when(refreshRepository.findByRefreshToken(UUID.fromString(refreshToken.getValue()))).thenThrow(UnauthorizedException.class);

        assertThrows(UnauthorizedException.class, () -> refreshService.refresh(request, response));
    }

    @Test
    @DisplayName("Deve retornar InvalidTokenException quando refresh token estiver expirado")
    void refreshInvalidTokenExceptionExpiredRefreshTokenTest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        Cookie refreshToken = new Cookie("refreshToken", "1c465b2a-f088-48a6-a20f-cb54b3d4f571");
        request.setCookies(refreshToken);

        UserEntity user = UserFactory.buildUserEntity();
        RefreshTokenEntity refreshTokenEntity = RefreshTokenFactory.buildRefreshTokenEntityExpired(user);

        when(refreshRepository.findByRefreshToken(UUID.fromString(refreshToken.getValue()))).thenReturn(Optional.of(refreshTokenEntity));

        assertThrows(InvalidTokenException.class, () -> refreshService.refresh(request, response));
    }

    @Test
    @DisplayName("Deve verificar se o usuário tem um refresh token e retorna um")
    void verifyExistRefreshTokenOfUserSuccessTest() {
        UserEntity user = UserFactory.buildUserEntity();
        UUID userId = user.getId();

        RefreshTokenEntity refreshTokenEntity = RefreshTokenFactory.buildRefreshTokenEntity(user);

        when(refreshRepository.findByUserId(userId)).thenReturn(Optional.of(refreshTokenEntity));

        Optional<RefreshTokenEntity> response = refreshService.verifyExistRefreshTokenOfUser(userId);

        verify(refreshRepository, times(1)).findByUserId(userId);
        assertTrue(response.isPresent());
        assertEquals(UUID.fromString("397eec71-360d-4626-805c-6640be635672"), response.get().getRefreshToken());
    }

    @Test
    @DisplayName("Deve verificar se o usuário tem um refresh token e não retorna um")
    void verifyExistRefreshTokenOfUserWithoutRefreshTokenTest() {
        UserEntity user = UserFactory.buildUserEntity();
        UUID userId = user.getId();

        when(refreshRepository.findByUserId(userId)).thenReturn(Optional.empty());

        Optional<RefreshTokenEntity> response = refreshService.verifyExistRefreshTokenOfUser(userId);

        verify(refreshRepository, times(1)).findByUserId(userId);
        assertThat(response).isEmpty();
    }
}
