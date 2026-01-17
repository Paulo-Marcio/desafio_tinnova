
package com.tinnova.desafio.security;

import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public Usuario autenticar(String token) {
        if (token == null || token.isBlank()) return null;

        if ("admin-token".equals(token)) {
            return new Usuario("admin", Role.ADMIN);
        }
        if ("user-token".equals(token)) {
            return new Usuario("user", Role.USER);
        }
        return null;
    }
}
