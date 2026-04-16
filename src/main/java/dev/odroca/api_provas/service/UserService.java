package dev.odroca.api_provas.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.odroca.api_provas.dto.users.RequestAddRole;
import dev.odroca.api_provas.dto.users.ResponseAddRole;
import dev.odroca.api_provas.entity.UserEntity;
import dev.odroca.api_provas.exception.UserNotFoundException;
import dev.odroca.api_provas.repository.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    public ResponseAddRole addRole(UUID userId, RequestAddRole request) {
        UserEntity user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        
        user.setRole(request.role());

        userRepository.save(user);
        return new ResponseAddRole(userId, "Função do usuário atualizada.");
    }

}
                                               