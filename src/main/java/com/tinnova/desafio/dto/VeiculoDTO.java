
package com.tinnova.desafio.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class VeiculoDTO {

    private Long id;

    @NotBlank
    private String marca;

    @NotBlank
    private String modelo;

    @NotNull
    private Integer ano;

    @NotBlank
    private String cor;

    @NotBlank
    @Pattern(
        regexp = "^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$",
        message = "Formato de placa inválida, siga o padrão mercosul ex: AAA1A12"
    )
    private String placa;

    @NotNull
    private BigDecimal precoEmReais;

    private BigDecimal precoUsd;
}
