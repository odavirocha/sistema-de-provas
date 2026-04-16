package dev.odroca.api_provas.service.user;

import dev.odroca.api_provas.dto.users.RequestAddRole;
import dev.odroca.api_provas.dto.users.ResponseAddRole;
import dev.odroca.api_provas.entity.UserEntity;
import dev.odroca.api_provas.enums.Role;
import dev.odroca.api_provas.exception.UserNotFoundException;
import dev.odroca.api_provas.repository.UserRepository;
import dev.odroca.api_provas.service.UserService;
import dev.odroca.api_provas.service.utils.UserFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Test
    @DisplayName("Deve adicionar uma role a um usuário")
    void addRoleSuccessTest() {
        UUID userId = UUID.fromString("a35a647b-6a7d-4cdc-b92e-87c5be376ee7");
        UserEntity user = UserFactory.buildUserEntity();
        RequestAddRole request = new RequestAddRole(Role.MODERATOR);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        ResponseAddRole response = userService.addRole(userId, request);

        verify(userRepository, times(1)).save(user);
        assertEquals(userId, response.userId());
        assertEquals("Função do usuário atualizada.", response.message());
    }

    @Test
    @DisplayName("Deve retornar UserNotFoundException quando o usuário não existir")
    void addRoleUserNotFoundExceptionTest() {
        UUID userId = UUID.fromString("a35a647b-6a7d-4cdc-b92e-87c5be376ee8");
        RequestAddRole request = new RequestAddRole(Role.MODERATOR);

        assertThrows(UserNotFoundException.class, () -> userService.addRole(userId, request));
    }

}
