package com.sees.projeto.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;
import com.sees.projeto.model.User;

@Service
public class UserService {

    // Armazenamento em memória (email → User)
    private final Map<String, User> usuarios = new ConcurrentHashMap<>();

    /**
     * Registra um novo usuário.
     * @return true se registrado com sucesso, false se email já existe.
     */
    public boolean registrar(String nome, String email, String senha) {
        if (usuarios.containsKey(email)) {
            return false; // email já cadastrado
        }
        usuarios.put(email, new User(nome, email, senha));
        return true;
    }

    /**
     * Autentica o usuário.
     * @return User se credenciais válidas, null caso contrário.
     */
    public User autenticar(String email, String senha) {
        User user = usuarios.get(email);
        if (user != null && user.getSenha().equals(senha)) {
            return user;
        }
        return null;
    }

    /**
     * Busca usuário por email.
     */
    public User buscarPorEmail(String email) {
        return usuarios.get(email);
    }
   
}