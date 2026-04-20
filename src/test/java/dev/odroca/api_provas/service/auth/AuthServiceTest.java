package dev.odroca.api_provas.service.auth;

import dev.odroca.api_provas.dto.login.LoginRequestDTO;
import dev.odroca.api_provas.dto.login.LoginResponseDTO;
import dev.odroca.api_provas.dto.signup.SignupRequestDTO;
import dev.odroca.api_provas.dto.signup.SignupResponseDTO;
import dev.odroca.api_provas.entity.RefreshTokenEntity;
import dev.odroca.api_provas.entity.UserEntity;
import dev.odroca.api_provas.exception.EmailAlreadyExistsException;
import dev.odroca.api_provas.exception.InvalidCredentialsException;
import dev.odroca.api_provas.repository.UserRepository;
import dev.odroca.api_provas.service.AuthService;
import dev.odroca.api_provas.service.RefreshTokenService;
import dev.odroca.api_provas.service.utils.RefreshTokenFactory;
import dev.odroca.api_provas.service.utils.UserFactory;
import dev.odroca.api_provas.utils.CookieUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    AuthService authService;

    @Mock
    UserRepository userRepository;
    @Mock
    RefreshTokenService refreshService;
    @Mock
    JwtEncoder jwt;
    @Mock
    CookieUtil cookie;
    @Mock
    BCryptPasswordEncoder bcrypt;

    @Test
    @DisplayName("Deve fazer criar uma conta com sucesso")
    void signupSuccessTest() {
        SignupRequestDTO request = new SignupRequestDTO("example@test.com", "123@123!abc");

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());

        SignupResponseDTO respose = authService.signup(request);
        assertEquals("Conta criada com sucesso!", respose.message());
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Deve retornar EmailAlreadyExistsException quando o usuário já existir")
    void signupEmailAlreadyExistsExceptionTest() {
        SignupRequestDTO request = new SignupRequestDTO("example@test.com", "123@123!abc");
        UserEntity user = UserFactory.buildUserEntity();

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));

        assertThrows(EmailAlreadyExistsException.class, () -> authService.signup(request));
    }

    @Test
    @DisplayName("Deve retornar sucesso ao fazer login")
    void loginFirstTimeSuccessTest() {
        UserEntity user = UserFactory.buildUserEntity();
        LoginRequestDTO request = new LoginRequestDTO("example@test.com", "123@123!abc");
        MockHttpServletResponse responseServlet = new MockHttpServletResponse();
        RefreshTokenEntity refreshTokenEntity = RefreshTokenFactory.buildRefreshTokenEntity(user);

        Jwt jwtMock = mock(Jwt.class);

        when(jwtMock.getTokenValue()).thenReturn("token-falso");
        when(jwt.encode(any(JwtEncoderParameters.class))).thenReturn(jwtMock);
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(bcrypt.matches(request.password(), user.getPassword())).thenReturn(true);
        when(refreshService.verifyExistRefreshTokenOfUser(user.getId())).thenReturn(Optional.of(refreshTokenEntity)); // Assumindo que já fez o primeiro login
        when(refreshService.createRefreshToken(eq(user))).thenReturn(refreshTokenEntity);

        LoginResponseDTO response = authService.login(request, responseServlet);

        assertEquals(user.getId().toString(), response.userId());
    }

    @Test
    @DisplayName("Deve retornar sucesso ao fazer login pela primeira vez")
    void loginSuccessTest() {
        UserEntity user = UserFactory.buildUserEntity();
        LoginRequestDTO request = new LoginRequestDTO("example@test.com", "123@123!abc");
        MockHttpServletResponse responseServlet = new MockHttpServletResponse();
        RefreshTokenEntity refreshToken = RefreshTokenFactory.buildRefreshTokenEntity(user);

        Jwt jwtMock = mock(Jwt.class);

        when(jwtMock.getTokenValue()).thenReturn("token-falso");
        when(jwt.encode(any(JwtEncoderParameters.class))).thenReturn(jwtMock);
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(bcrypt.matches(request.password(), user.getPassword())).thenReturn(true);
        when(refreshService.verifyExistRefreshTokenOfUser(user.getId())).thenReturn(Optional.empty()); // Assumindo que é o primeiro login
        when(refreshService.createRefreshToken(eq(user))).thenReturn(refreshToken);

        LoginResponseDTO response = authService.login(request, responseServlet);

        assertEquals(user.getId().toString(), response.userId());
    }

    @Test
    @DisplayName("Deve retornar InvalidCredentialsException quando o email não existir")
    void loginInvalidCredentialsExceptionUserDontExistTest() {
        LoginRequestDTO request = new LoginRequestDTO("example@teste.com", "123@123!abc");
        MockHttpServletResponse responseServlet = new MockHttpServletResponse();

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () -> authService.login(request, responseServlet));
    }

    @Test
    @DisplayName("Deve retornar InvalidCredentialsException se as senhas forem incompatíveis")
    void loginInvalidCredentialsExceptionNoMatchPasswordsTest() {
        UserEntity user = UserFactory.buildUserEntity();
        LoginRequestDTO request = new LoginRequestDTO("example@test.com", "123@123!abc");
        MockHttpServletResponse responseServlet = new MockHttpServletResponse();

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(bcrypt.matches(request.password(), user.getPassword())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> authService.login(request,responseServlet));
    }

}
