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

    /**
     * Este método cria um runner para testar os métodos CRUD quando o perfil "test" está ativo
     */
    @Bean
    @Profile("test")
    public CommandLineRunner testRunner(UserService userService, UserRepository userRepository) {
        return args -> {
            System.out.println("Iniciando testes CRUD de usuário...");

            // Limpar dados de teste anteriores
            limparDadosTeste(userRepository);

            // Teste de CREATE (Registro de usuário)
            testarRegistroUsuario(userService);

            // Teste de READ (Buscar usuário)
            testarBuscaUsuario(userService);

            // Teste de UPDATE (Atualizar usuário)
            testarAtualizacaoUsuario(userService);

            // Teste de DELETE (Remover usuário)
            testarRemocaoUsuario(userService);

            System.out.println("Testes CRUD concluídos com sucesso!");
        };
    }

    private void limparDadosTeste(UserRepository userRepository) {
        System.out.println("Limpando dados de teste...");
        userRepository.deleteByEmail("teste@styleme.com");
        System.out.println("Dados de teste limpos.");
    }

    private void testarRegistroUsuario(UserService userService) {
        System.out.println("\n--- TESTE DE REGISTRO DE USUÁRIO ---");
        try {
            Cliente cliente = new Cliente();
            cliente.setNome("Usuário Teste");
            cliente.setEmail("teste@styleme.com");
            cliente.setSenha("senha123");

            Cliente novoCliente = userService.registerUser(cliente);

            System.out.println("Usuário registrado com sucesso!");
            System.out.println("ID: " + novoCliente.getId());
            System.out.println("Nome: " + novoCliente.getNome());
            System.out.println("Email: " + novoCliente.getEmail());

            // Verificar se a senha foi criptografada
            System.out.println("Senha criptografada: " + novoCliente.getSenha().startsWith("$2a$"));

            // Teste de autenticação
            boolean autenticado = userService.authenticateUser("teste@styleme.com", "senha123");
            System.out.println("Autenticação: " + (autenticado ? "Sucesso" : "Falha"));
        } catch (Exception e) {
            System.err.println("Erro no teste de registro: " + e.getMessage());
        }
    }

    private void testarBuscaUsuario(UserService userService) {
        System.out.println("\n--- TESTE DE BUSCA DE USUÁRIO ---");
        try {
            // Buscar todos os usuários
            System.out.println("Total de usuários: " + userService.findAllUsers().size());

            // Buscar usuário específico
            Optional<Cliente> clienteOpt = userService.findAllUsers().stream()
                .filter(c -> "teste@styleme.com".equals(c.getEmail()))
                .findFirst();

            if (clienteOpt.isPresent()) {
                Cliente cliente = clienteOpt.get();
                System.out.println("Usuário encontrado por email: " + cliente.getNome());

                // Buscar por ID
                Optional<Cliente> porId = userService.findUserById(cliente.getId());
                System.out.println("Usuário encontrado por ID: " +
                    (porId.isPresent() ? "Sim" : "Não"));
            } else {
                System.err.println("Usuário de teste não encontrado!");
            }
        } catch (Exception e) {
            System.err.println("Erro no teste de busca: " + e.getMessage());
        }
    }

    private void testarAtualizacaoUsuario(UserService userService) {
        System.out.println("\n--- TESTE DE ATUALIZAÇÃO DE USUÁRIO ---");
        try {
            // Buscar o usuário de teste
            Optional<Cliente> clienteOpt = userService.findAllUsers().stream()
                .filter(c -> "teste@styleme.com".equals(c.getEmail()))
                .findFirst();

            if (clienteOpt.isPresent()) {
                Cliente cliente = clienteOpt.get();
                // Atualizar dados
                cliente.setNome("Usuário Teste Atualizado");

                Cliente atualizado = userService.updateUser(cliente);
                System.out.println("Usuário atualizado: " + atualizado.getNome());
            } else {
                System.err.println("Usuário de teste não encontrado para atualização!");
            }
        } catch (Exception e) {
            System.err.println("Erro no teste de atualização: " + e.getMessage());
        }
    }

    private void testarRemocaoUsuario(UserService userService) {
        System.out.println("\n--- TESTE DE REMOÇÃO DE USUÁRIO ---");
        try {
            // Buscar o usuário de teste
            Optional<Cliente> clienteOpt = userService.findAllUsers().stream()
                .filter(c -> "teste@styleme.com".equals(c.getEmail()))
                .findFirst();

            if (clienteOpt.isPresent()) {
                Cliente cliente = clienteOpt.get();
                // Remover usuário
                boolean removido = userService.deleteUser(cliente.getId());
                System.out.println("Usuário removido: " + (removido ? "Sim" : "Não"));

                // Verificar se foi removido
                Optional<Cliente> verificacao = userService.findUserById(cliente.getId());
                System.out.println("Verificação pós-remoção: " +
                    (verificacao.isEmpty() ? "Removido com sucesso" : "Falha na remoção"));
            } else {
                System.err.println("Usuário de teste não encontrado para remoção!");
            }
        } catch (Exception e) {
            System.err.println("Erro no teste de remoção: " + e.getMessage());
        }
    }
}