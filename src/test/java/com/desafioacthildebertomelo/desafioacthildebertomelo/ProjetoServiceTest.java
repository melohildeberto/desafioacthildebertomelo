package com.desafioacthildebertomelo.desafioacthildebertomelo;

import com.desafioacthildebertomelo.desafioacthildebertomelo.models.Projeto;
import com.desafioacthildebertomelo.desafioacthildebertomelo.models.StatusProjeto;
import com.desafioacthildebertomelo.desafioacthildebertomelo.repositories.ProjetoRepository;
import com.desafioacthildebertomelo.desafioacthildebertomelo.services.ProjetoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjetoServiceTest {

    private ProjetoRepository projetoRepository;
    private ProjetoService projetoService;

    @BeforeEach
    void setUp() {
        projetoRepository = Mockito.mock(ProjetoRepository.class);
        projetoService = new ProjetoService(projetoRepository);
    }

    @Test
    void deveCriarProjetoComStatusAINICIAR() {
        Projeto projeto = Projeto.builder()
                .nome("Novo Projeto")
                .inicioPrevisto(LocalDate.now().plusDays(5))
                .terminoPrevisto(LocalDate.now().plusDays(10))
                .build();

        when(projetoRepository.save(any(Projeto.class))).thenAnswer(inv -> inv.getArgument(0));

        Projeto salvo = projetoService.criarProjeto(projeto);

        assertEquals(StatusProjeto.AINICIAR, salvo.getStatus());
        verify(projetoRepository, times(1)).save(projeto);
    }

    @Test
    void deveAtualizarProjetoERecalcularStatusParaAtrasado() {
        UUID id = UUID.randomUUID();
        Projeto projetoExistente = Projeto.builder()
                .id(id)
                .nome("Projeto Antigo")
                .inicioPrevisto(LocalDate.now().minusDays(10))
                .terminoPrevisto(LocalDate.now().minusDays(1))
                .build();

        Projeto projetoAtualizado = Projeto.builder()
                .nome("Projeto Atualizado")
                .inicioPrevisto(LocalDate.now().minusDays(10))
                .terminoPrevisto(LocalDate.now().minusDays(1))
                .build();

        when(projetoRepository.findById(id)).thenReturn(Optional.of(projetoExistente));
        when(projetoRepository.save(any(Projeto.class))).thenAnswer(inv -> inv.getArgument(0));

        Projeto salvo = projetoService.atualizarProjeto(id, projetoAtualizado);

        assertEquals(StatusProjeto.ATRASADO, salvo.getStatus());
        verify(projetoRepository, times(1)).save(projetoExistente);
    }

    @Test
    void deveLancarErroAoBuscarProjetoInexistente() {
        UUID id = UUID.randomUUID();
        when(projetoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> projetoService.buscarPorId(id));
    }
}
