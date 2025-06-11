package com.styleme.projeto.controller;

import com.styleme.projeto.entity.Avatar;
import com.styleme.projeto.entity.Cliente;
import com.styleme.projeto.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    // READ - Buscar todos os usuários
    @GetMapping
    public ResponseEntity<List<Cliente>> getAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    // READ - Buscar usuário por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable UUID id) {
        return userService.findUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE - Atualizar usuário
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable UUID id, @RequestBody Cliente cliente) {
        try {
            cliente.setId(id); // Garante que o ID do path é usado
            Cliente clienteAtualizado = userService.updateUser(cliente);
            return ResponseEntity.ok(clienteAtualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE - Remover usuário
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        if (userService.deleteUser(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}