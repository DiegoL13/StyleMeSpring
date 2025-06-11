package com.styleme.projeto.controller;

import com.styleme.projeto.entity.Avatar;
import com.styleme.projeto.entity.Cliente;
import com.styleme.projeto.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Cliente cliente) {
        try {
            Cliente novoCliente = userService.registerUser(cliente);
            return ResponseEntity.ok(novoCliente);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String senha) {
        return userService.authenticateUser(email, senha)
                ? ResponseEntity.ok("Login bem-sucedido!")
                : ResponseEntity.status(401).body("Credenciais inválidas!");
    }

}
