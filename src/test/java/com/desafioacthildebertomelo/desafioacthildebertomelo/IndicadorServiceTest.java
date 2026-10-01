package com.desafioacthildebertomelo.desafioacthildebertomelo;

import com.desafioacthildebertomelo.desafioacthildebertomelo.models.Projeto;
import com.desafioacthildebertomelo.desafioacthildebertomelo.models.StatusProjeto;
import com.desafioacthildebertomelo.desafioacthildebertomelo.repositories.ProjetoRepository;
import com.desafioacthildebertomelo.desafioacthildebertomelo.services.IndicadorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IndicadorServiceTest {

    private ProjetoRepository projetoRepository;
    private IndicadorService indicadorService;

    @BeforeEach
    void setUp() {
        projetoRepository = Mockito.mock(ProjetoRepository.class);
        indicadorService = new IndicadorService(projetoRepository);
    }

    @Test
    void deveContarProjetosPorStatus() {
        Projeto p1 = Projeto.builder().status(StatusProjeto.EM_ANDAMENTO).build();
        Projeto p2 = Projeto.builder().status(StatusProjeto.CONCLUIDO).build();
        Projeto p3 = Projeto.builder().status(StatusProjeto.CONCLUIDO).build();

        when(projetoRepository.findAll()).thenReturn(List.of(p1, p2, p3));

        Map<StatusProjeto, Long> resultado = indicadorService.contarProjetosPorStatus();

        assertEquals(1, resultado.get(StatusProjeto.EM_ANDAMENTO));
        assertEquals(2, resultado.get(StatusProjeto.CONCLUIDO));
    }

    @Test
    void deveCalcularMediaDiasAtrasoPorStatus() {
        Projeto p1 = Projeto.builder()
                .status(StatusProjeto.CONCLUIDO)
                .terminoPrevisto(LocalDate.now().minusDays(5))
                .terminoRealizado(LocalDate.now())
                .build();

        Projeto p2 = Projeto.builder()
                .status(StatusProjeto.CONCLUIDO)
                .terminoPrevisto(LocalDate.now().minusDays(10))
                .terminoRealizado(LocalDate.now())
                .build();

        when(projetoRepository.findAll()).thenReturn(List.of(p1, p2));

        Map<StatusProjeto, Double> resultado = indicadorService.mediaDiasAtrasoPorStatus();

        assertTrue(resultado.get(StatusProjeto.CONCLUIDO) > 0);
    }

    @Test
    void deveRetornarPercentualConcluido() {
        Projeto p1 = Projeto.builder().status(StatusProjeto.CONCLUIDO).build();
        //Projeto p2 = Projeto.builder().status(StatusProjeto.EM_ANDAMENTO).build();

        when(projetoRepository.count()).thenReturn(2L);
        when(projetoRepository.findByStatus(StatusProjeto.CONCLUIDO)).thenReturn(List.of(p1));

        double percentual = indicadorService.percentualConcluido();

        assertEquals(50.0, percentual);
    }

    @Test
    void deveRetornarZeroQuandoNaoHaProjetos() {
        when(projetoRepository.count()).thenReturn(0L);

        double percentual = indicadorService.percentualConcluido();

        assertEquals(0.0, percentual);
    }

    @Test
    void deveCalcularMediaDuracaoProjetos() {
        Projeto p1 = Projeto.builder()
                .inicioRealizado(LocalDate.now().minusDays(10))
                .terminoRealizado(LocalDate.now())
                .build();

        Projeto p2 = Projeto.builder()
                .inicioRealizado(LocalDate.now().minusDays(20))
                .terminoRealizado(LocalDate.now())
                .build();

        when(projetoRepository.findAll()).thenReturn(List.of(p1, p2));

        double media = indicadorService.mediaDuracaoProjetos();

        assertTrue(media >= 10 && media <= 20);
    }

    @Test
    void deveRetornarZeroQuandoNaoHaDatasValidasParaDuracao() {
        Projeto p1 = Projeto.builder().inicioRealizado(null).terminoRealizado(null).build();

        when(projetoRepository.findAll()).thenReturn(List.of(p1));

        double media = indicadorService.mediaDuracaoProjetos();

        assertEquals(0.0, media);
    }
}
