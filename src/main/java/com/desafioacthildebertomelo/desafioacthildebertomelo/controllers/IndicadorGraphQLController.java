package com.desafioacthildebertomelo.desafioacthildebertomelo.controllers;

import com.desafioacthildebertomelo.desafioacthildebertomelo.models.StatusProjeto;
import com.desafioacthildebertomelo.desafioacthildebertomelo.services.IndicadorService;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class IndicadorGraphQLController {

    private final IndicadorService indicadorService;

    public IndicadorGraphQLController(IndicadorService indicadorService) {
        this.indicadorService = indicadorService;
    }

    @QueryMapping
    public Map<StatusProjeto, Long> projetosPorStatus() {
        return indicadorService.contarProjetosPorStatus();
    }

    @QueryMapping
    public Map<StatusProjeto, Double> mediaAtrasoPorStatus() {
        return indicadorService.mediaDiasAtrasoPorStatus();
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
