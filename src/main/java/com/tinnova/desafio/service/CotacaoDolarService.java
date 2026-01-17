package com.tinnova.desafio.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Service
public class CotacaoDolarService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Cacheable("cotacao-dolar")
    public BigDecimal obterCotacaoAtual() {
        try {
            Map response = restTemplate.getForObject(
                    "https://economia.awesomeapi.com.br/json/last/USD-BRL",
                    Map.class
            );

            Map usdbrl = (Map) response.get("USDBRL");
            return new BigDecimal(usdbrl.get("bid").toString());

        } catch (Exception ex) {
            return fallback();
        }
    }

    private BigDecimal fallback() {
        Map response = restTemplate.getForObject(
                "https://api.frankfurter.app/latest?from=USD&to=BRL",
                Map.class
        );

        Map rates = (Map) response.get("rates");
        return new BigDecimal(rates.get("BRL").toString());
    }

    public BigDecimal converterParaUsd(BigDecimal valorEmReais) {
        return valorEmReais.divide(obterCotacaoAtual(), 2, RoundingMode.HALF_UP);
    }
}
