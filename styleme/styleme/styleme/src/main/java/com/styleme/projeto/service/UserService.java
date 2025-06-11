package com.styleme.projeto.service;

import com.styleme.projeto.entity.Cliente;
import com.styleme.projeto.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


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

}