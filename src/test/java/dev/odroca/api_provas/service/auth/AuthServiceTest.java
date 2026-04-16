package dev.odroca.api_provas.service.auth;

import dev.odroca.api_provas.dto.signup.SignupRequestDTO;
import dev.odroca.api_provas.dto.signup.SignupResponseDTO;
import dev.odroca.api_provas.entity.UserEntity;
import dev.odroca.api_provas.exception.EmailAlreadyExistsException;
import dev.odroca.api_provas.repository.UserRepository;
import dev.odroca.api_provas.service.AuthService;
import dev.odroca.api_provas.service.RefreshTokenService;
import dev.odroca.api_provas.service.utils.UserFactory;
import dev.odroca.api_provas.utils.CookieUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    BCryptPasswordEncoder bcrypt;
    @Mock
    CookieUtil cookie;

    @Test
    @DisplayName("Deve fazer criar uma conta com sucesso")
    void signupSuccessTest() {
        SignupRequestDTO request = new SignupRequestDTO("example@example.com", "123@123!abc");

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());

        SignupResponseDTO respose = authService.signup(request);
        assertEquals("Conta criada com sucesso!", respose.message());
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Deve retornar EmailAlreadyExistsException quando o usuário já existir")
    void signupEmailAlreadyExistsExceptionTest() {
        SignupRequestDTO request = new SignupRequestDTO("example@example.com", "123@123!abc");
        UserEntity user = UserFactory.buildUserEntity();

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));

        assertThrows(EmailAlreadyExistsException.class, () -> authService.signup(request));
    }

}
