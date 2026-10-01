package com.desafioacthildebertomelo.desafioacthildebertomelo.controllers;

import com.desafioacthildebertomelo.desafioacthildebertomelo.models.StatusProjeto;
import com.desafioacthildebertomelo.desafioacthildebertomelo.services.IndicadorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/indicadores")
@RequiredArgsConstructor
public class IndicadorController {

    private final IndicadorService indicadorService;

    @GetMapping("/projetos-por-status")
    public ResponseEntity<Map<StatusProjeto, Long>> projetosPorStatus() {
        return ResponseEntity.ok(indicadorService.contarProjetosPorStatus());
    }

    @GetMapping("/media-atraso")
    public ResponseEntity<Map<StatusProjeto, Double>> mediaAtrasoPorStatus() {
        return ResponseEntity.ok(indicadorService.mediaDiasAtrasoPorStatus());
    }

    @GetMapping("/percentual-concluido")
    public ResponseEntity<Double> percentualConcluido() {
        return ResponseEntity.ok(indicadorService.percentualConcluido());
    }

    // Diferencial: tempo médio de execução dos projetos
    @GetMapping("/media-duracao")
    public ResponseEntity<Double> mediaDuracaoProjetos() {
        return ResponseEntity.ok(indicadorService.mediaDuracaoProjetos());
    }
}
