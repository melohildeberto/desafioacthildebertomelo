package com.desafioacthildebertomelo.desafioacthildebertomelo.controllers;

import com.desafioacthildebertomelo.desafioacthildebertomelo.models.Projeto;
import com.desafioacthildebertomelo.desafioacthildebertomelo.models.Responsavel;
import com.desafioacthildebertomelo.desafioacthildebertomelo.models.graphqls.StatusDelay;
import com.desafioacthildebertomelo.desafioacthildebertomelo.models.StatusProjeto;
import com.desafioacthildebertomelo.desafioacthildebertomelo.models.graphqls.StatusCount;
import com.desafioacthildebertomelo.desafioacthildebertomelo.services.IndicadorService;

import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Controller
public class IndicadorGraphQLController {

    private final IndicadorService indicadorService;

    public IndicadorGraphQLController(IndicadorService indicadorService) {
        this.indicadorService = indicadorService;
    }

    @QueryMapping
    public List<StatusCount> projetosPorStatus() {
        Map<StatusProjeto, Long> mapa = indicadorService.contarProjetosPorStatus();
        return mapa.entrySet().stream()
                .map(e -> new StatusCount(e.getKey().name(), e.getValue()))
                .toList();
    }

    @QueryMapping
    public List<StatusDelay> mediaAtrasoPorStatus() {
        Map<StatusProjeto, Double> mapa = indicadorService.mediaDiasAtrasoPorStatus();
        return mapa.entrySet().stream()
                .map(e -> new StatusDelay(e.getKey().name(), e.getValue()))
                .toList();
    }

    @QueryMapping
    public Double percentualConcluido() {
        return indicadorService.percentualConcluido();
    }

    @QueryMapping
    public Double mediaDuracaoProjetos() {
        return indicadorService.mediaDuracaoProjetos();
    }

}
