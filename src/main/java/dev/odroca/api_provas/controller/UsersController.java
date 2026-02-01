package dev.odroca.api_provas.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import dev.odroca.api_provas.dto.users.RequestAddRoles;
import dev.odroca.api_provas.dto.users.ResponseAddRoles;
import dev.odroca.api_provas.service.UserService;

import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/users")
public class UsersController {
    
    @Autowired
    private UserService userService;

    @PatchMapping("/role/{userId}")
    public ResponseEntity<ResponseAddRoles> addRole(@PathVariable UUID userId, @RequestBody RequestAddRoles request) {
        ResponseAddRoles response = userService.addRole(userId, request);
        return new ResponseEntity<ResponseAddRoles>(response, HttpStatus.CREATED);
    }

}
