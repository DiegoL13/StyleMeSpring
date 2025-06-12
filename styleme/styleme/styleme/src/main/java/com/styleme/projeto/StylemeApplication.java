package com.styleme.projeto;

import com.styleme.projeto.entity.Cliente;
import com.styleme.projeto.repository.UserRepository;
import com.styleme.projeto.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
public class StylemeApplication {

    public static void main(String[] args) {
        SpringApplication.run(StylemeApplication.class, args);
    }
}