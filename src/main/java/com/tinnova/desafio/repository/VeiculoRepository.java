
package com.tinnova.desafio.repository;

import com.tinnova.desafio.model.Veiculo;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {

    boolean existsByPlaca(String placa);
    Optional<Veiculo> findByIdAndAtivoTrue(Long id);

    @Query("""
        SELECT v FROM Veiculo v
        WHERE v.ativo = true
          AND (:marca IS NULL OR v.marca = :marca)
          AND (:ano IS NULL OR v.ano = :ano)
          AND (:cor IS NULL OR v.cor = :cor)
          AND (:minPreco IS NULL OR v.precoUsd >= :minPreco)
          AND (:maxPreco IS NULL OR v.precoUsd <= :maxPreco)
    """)
    Page<Veiculo> filtrar(
        @Param("marca") String marca,
        @Param("ano") Integer ano,
        @Param("cor") String cor,
        @Param("minPreco") BigDecimal minPreco,
        @Param("maxPreco") BigDecimal maxPreco,
        Pageable pageable
    );


}
