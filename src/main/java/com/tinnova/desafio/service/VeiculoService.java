package com.tinnova.desafio.service;


import com.tinnova.desafio.dto.VeiculoDTO;
import com.tinnova.desafio.exception.ConflitoException;
import com.tinnova.desafio.exception.RecursoNaoEncontradoException;
import com.tinnova.desafio.exception.RequisicaoInvalidaException;
import com.tinnova.desafio.model.Veiculo;
import com.tinnova.desafio.repository.VeiculoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class VeiculoService {

    private final VeiculoRepository repository;
    private final CotacaoDolarService cotacaoService;

    public VeiculoService(VeiculoRepository repository,
                          CotacaoDolarService cotacaoService) {
        this.repository = repository;
        this.cotacaoService = cotacaoService;
    }

    public VeiculoDTO criar(VeiculoDTO dto) {
        if (repository.existsByPlaca(dto.getPlaca())) {
            throw new ConflitoException("Placa já cadastrada");
        }

        Veiculo veiculo = new Veiculo();
        BeanUtils.copyProperties(dto, veiculo);

        BigDecimal cotacao = cotacaoService.obterCotacaoAtual();
        BigDecimal precoUsd = dto.getPrecoEmReais().divide(cotacao, 2, RoundingMode.HALF_UP);

        veiculo.setId(null);
        veiculo.setPrecoUsd(precoUsd);
        veiculo.setAtivo(true);
        Veiculo salvo = repository.save(veiculo);

        VeiculoDTO resp = new VeiculoDTO();
        BeanUtils.copyProperties(salvo, resp);
        resp.setPrecoEmReais(dto.getPrecoEmReais());

        return resp;
    }

    public Page<VeiculoDTO> filtrar(String marca, Integer ano, String cor,
                                    BigDecimal minPreco, BigDecimal maxPreco,
                                    Pageable pageable) {
        return repository.filtrar(marca, ano, cor, minPreco, maxPreco, pageable)
                .map(this::toDto);
    }

    public VeiculoDTO buscar(Long id) {
        Veiculo veiculo = repository.findById(id)
                .filter(Veiculo::getAtivo)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Veículo não encontrado"));
        return toDto(veiculo);
    }
    @Transactional
    public VeiculoDTO atualizarParcial(Long id, VeiculoDTO dto) {

        Veiculo veiculo = repository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Veículo não encontrado"));

        validarAtualizacaoParcial(dto, veiculo);
        return toDto(repository.save(veiculo));
    }

    private void validarAtualizacaoParcial(VeiculoDTO dto, Veiculo veiculo) {
        if (dto.getMarca() != null) {
            veiculo.setMarca(dto.getMarca());
        }

        if (dto.getModelo() != null) {
            veiculo.setModelo(dto.getModelo());
        }

        if (dto.getAno() != null) {
            veiculo.setAno(dto.getAno());
        }

        if (dto.getCor() != null) {
            veiculo.setCor(dto.getCor());
        }

        if (dto.getPlaca() != null) {
            String novaPlaca = dto.getPlaca();
            if (!novaPlaca.equals(veiculo.getPlaca()) && repository.existsByPlaca(dto.getPlaca())) {
                throw new ConflitoException("Placa já cadastrada");
            }
            veiculo.setPlaca(novaPlaca);
        }

        if (dto.getPrecoEmReais() != null) {
            veiculo.setPrecoUsd(cotacaoService.converterParaUsd(dto.getPrecoEmReais()));
        }
    }

    @Transactional
    public VeiculoDTO atualizar(Long id, VeiculoDTO dto) {

        Veiculo veiculo = repository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new  RecursoNaoEncontradoException ("Veículo não encontrado"));

        validarAtualizacao(dto, veiculo);
        return toDto(repository.save(veiculo));
    }

    private void validarAtualizacao(VeiculoDTO dto, Veiculo veiculo) {

        if (dto.getMarca() == null ||
                dto.getModelo() == null ||
                dto.getAno() == null ||
                dto.getCor() == null ||
                dto.getPlaca() == null ||
                dto.getPrecoEmReais() == null) {

            throw new RequisicaoInvalidaException("PUT exige que todos os campos sejam alterados");
        }

            if (!dto.getPlaca().equals(veiculo.getPlaca()) && repository.existsByPlaca(dto.getPlaca())) {
                throw new ConflitoException("Placa já cadastrada");

            }

        veiculo.setMarca(dto.getMarca());
        veiculo.setModelo(dto.getModelo());
        veiculo.setAno(dto.getAno());
        veiculo.setCor(dto.getCor());
        veiculo.setPlaca(dto.getPlaca());
        veiculo.setPrecoUsd(cotacaoService.converterParaUsd(dto.getPrecoEmReais()));
    }


    public void remover(Long id) {
        Veiculo veiculo = repository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Veículo não encontrado"));
        veiculo.setAtivo(false);
        repository.save(veiculo);
    }

    private VeiculoDTO toDto(Veiculo veiculo) {
        VeiculoDTO dto = new VeiculoDTO();
        BeanUtils.copyProperties(veiculo, dto);
        return dto;
    }
}
