package dev.odroca.api_provas.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.odroca.api_provas.dto.users.RequestAddRoles;
import dev.odroca.api_provas.dto.users.ResponseAddRoles;
import dev.odroca.api_provas.entity.UserEntity;
import dev.odroca.api_provas.exception.UserNotFoundException;
import dev.odroca.api_provas.repository.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    public ResponseAddRoles addRole(UUID userId, RequestAddRoles request) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        
        user.setRole(request.role());

        userRepository.save(user);
        return new ResponseAddRoles(userId, "Função do usuário atualizada.");
    }

}
