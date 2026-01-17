
package com.tinnova.desafio.security;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Usuario {
    private String perfil;
    private Role role;
}
