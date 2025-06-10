package com.styleme.projeto.service;

import com.styleme.projeto.entity.Cliente;
import com.styleme.projeto.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public Cliente registerUser(Cliente cliente) {
        cliente.setSenha(passwordEncoder.encode(cliente.getSenha())); // Criptografando senha
        return userRepository.save(cliente);
    }

    public boolean authenticateUser(String email, String senha) {
        return userRepository.findByEmail(email).map(cliente -> passwordEncoder.matches(senha, cliente.getSenha())).orElse(false);
    }
}
