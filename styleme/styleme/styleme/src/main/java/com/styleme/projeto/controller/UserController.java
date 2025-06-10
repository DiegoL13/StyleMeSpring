package com.styleme.projeto.controller;

import com.styleme.projeto.entity.Cliente;
import com.styleme.projeto.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Cliente register(@RequestBody Cliente cliente) {
        return userService.registerUser(cliente);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String senha) {
        return userService.authenticateUser(email, senha)
                ? ResponseEntity.ok("Login bem-sucedido!")
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas!");
    }
}