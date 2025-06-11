package com.styleme.projeto.service;

import com.styleme.projeto.entity.Cliente;
import com.styleme.projeto.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Cliente registerUser(Cliente cliente) {
        if (userRepository.findByEmail(cliente.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado!");
        }
        cliente.setSenha(passwordEncoder.encode(cliente.getSenha())); // Criptografando senha
        return userRepository.save(cliente);
    }

    public boolean authenticateUser(String email, String senha) {
        Optional<Cliente> clienteOptional = userRepository.findByEmail(email);
        return clienteOptional.isPresent() && passwordEncoder.matches(senha, clienteOptional.get().getSenha());
    }

    public List<Cliente> findAllUsers() {
        return userRepository.findAll();
    }

    public Optional<Cliente> findUserById(UUID id) {
        return userRepository.findById(id);
    }

    @Transactional
    public Cliente updateUser(Cliente cliente) {
        if (!userRepository.existsById(cliente.getId())) {
            throw new IllegalArgumentException("Usuário não encontrado!");
        }

        Optional<Cliente> existingEmail = userRepository.findByEmail(cliente.getEmail());
        if (existingEmail.isPresent() && !existingEmail.get().getId().equals(cliente.getId())) {
            throw new IllegalArgumentException("Email já está sendo usado por outro usuário!");
        }

        if (cliente.getSenha() != null && !cliente.getSenha().startsWith("$2a$")) {
            cliente.setSenha(passwordEncoder.encode(cliente.getSenha()));
        }

        return userRepository.save(cliente);
    }

    @Transactional
    public boolean deleteUser(UUID id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}