
package com.tinnova.desafio.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "veiculos", uniqueConstraints = @UniqueConstraint(columnNames = "placa"))
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String marca;
    private String modelo;
    private Integer ano;
    private String cor;

    @Column(unique = true)
    private String placa;

    @Column(name = "preco_usd", nullable = false)
    private BigDecimal precoUsd;

    private Boolean ativo = true;
}
