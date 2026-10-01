package com.desafioacthildebertomelo.desafioacthildebertomelo.services;

import com.desafioacthildebertomelo.desafioacthildebertomelo.models.Projeto;
import com.desafioacthildebertomelo.desafioacthildebertomelo.models.StatusProjeto;
import com.desafioacthildebertomelo.desafioacthildebertomelo.repositories.ProjetoRepository;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class IndicadorService {

    private final ProjetoRepository projetoRepository;

    public IndicadorService(ProjetoRepository projetoRepository) {
        this.projetoRepository = projetoRepository;
    }

    public Map<StatusProjeto, Long> contarProjetosPorStatus() {
        return projetoRepository.findAll().stream()
                .collect(Collectors.groupingBy(Projeto::getStatus, Collectors.counting()));
    }

    public Map<StatusProjeto, Double> mediaDiasAtrasoPorStatus() {
        return projetoRepository.findAll().stream()
                .collect(Collectors.groupingBy(Projeto::getStatus,
                        Collectors.averagingDouble(Projeto::getDiasDeAtraso)));
    }

    public double percentualConcluido() {
        long total = projetoRepository.count();
        long concluidos = projetoRepository.findByStatus(StatusProjeto.CONCLUIDO).size();
        return total > 0 ? (concluidos * 100.0) / total : 0;
    }

    public double mediaDuracaoProjetos() {
        return projetoRepository.findAll().stream()
                .filter(p -> p.getInicioRealizado() != null && p.getTerminoRealizado() != null)
                .mapToLong(p -> ChronoUnit.DAYS.between(p.getInicioRealizado(), p.getTerminoRealizado()))
                .average()
                .orElse(0);
    }
}
