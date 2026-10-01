package com.desafioacthildebertomelo.desafioacthildebertomelo;

import com.desafioacthildebertomelo.desafioacthildebertomelo.models.Responsavel;
import com.desafioacthildebertomelo.desafioacthildebertomelo.models.Secretaria;
import com.desafioacthildebertomelo.desafioacthildebertomelo.repositories.ProjetoRepository;
import com.desafioacthildebertomelo.desafioacthildebertomelo.repositories.ResponsavelRepository;
import com.desafioacthildebertomelo.desafioacthildebertomelo.repositories.SecretariaRepository;
import com.desafioacthildebertomelo.desafioacthildebertomelo.services.ResponsavelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

class ResponsavelServiceTest {

    private ResponsavelRepository responsavelRepository;
    private SecretariaRepository secretariaRepository;
    private ProjetoRepository projetoRepository;
    private ResponsavelService responsavelService;

    @BeforeEach
    void setUp() {
        responsavelRepository = Mockito.mock(ResponsavelRepository.class);
        secretariaRepository = Mockito.mock(SecretariaRepository.class);
        projetoRepository = Mockito.mock(ProjetoRepository.class);
        responsavelService = new ResponsavelService(responsavelRepository, secretariaRepository, projetoRepository);
    }

    @Test
    void deveCriarResponsavelComSecretariaExistente() {
        UUID secretariaId = UUID.randomUUID();
        Secretaria secretaria = Secretaria.builder().id(secretariaId).nome("TI").email("ti@org.com").build();

        Responsavel novo = Responsavel.builder()
                .nome("João Silva")
                .email("joao@teste.com")
                .cargo("Analista")
                .secretaria(secretaria)
                .build();

        when(secretariaRepository.findById(secretariaId)).thenReturn(Optional.of(secretaria));
        when(responsavelRepository.existsByEmail("joao@teste.com")).thenReturn(false);
        when(responsavelRepository.save(novo)).thenReturn(novo);

        Responsavel salvo = responsavelService.criarResponsavel(novo);

        assertNotNull(salvo);
        assertEquals("joao@teste.com", salvo.getEmail());
        verify(responsavelRepository, times(1)).save(novo);
    }

    @Test
    void deveLancarErroAoCriarResponsavelComSecretariaInexistente() {
        UUID secretariaId = UUID.randomUUID();
        Secretaria secretaria = Secretaria.builder().id(secretariaId).build();

        Responsavel novo = Responsavel.builder()
                .nome("Maria")
                .email("maria@teste.com")
                .cargo("Gestora")
                .secretaria(secretaria)
                .build();

        when(secretariaRepository.findById(secretariaId)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> responsavelService.criarResponsavel(novo));

        assertTrue(ex.getMessage().contains("Secretaria não encontrada"));
        verify(responsavelRepository, never()).save(novo);
    }

    @Test
    void deveDeletarResponsavelSemVinculoComProjeto() {
        UUID id = UUID.randomUUID();
        Responsavel responsavel = Responsavel.builder().id(id).nome("Carlos").email("carlos@teste.com").cargo("Dev")
                .secretaria(Secretaria.builder().id(UUID.randomUUID()).nome("TI").email("ti@org.com").build())
                .build();

        when(responsavelRepository.findById(id)).thenReturn(Optional.of(responsavel));
        when(projetoRepository.existsByResponsaveisContains(responsavel)).thenReturn(false);
        doNothing().when(responsavelRepository).deleteById(id);

        responsavelService.deletarResponsavel(id);

        verify(responsavelRepository, times(1)).deleteById(id);
    }

    @Test
    void deveLancarErroAoDeletarResponsavelVinculadoAProjeto() {
        UUID id = UUID.randomUUID();
        Responsavel responsavel = Responsavel.builder().id(id).nome("Ana").email("ana@teste.com").cargo("Coordenadora")
                .secretaria(Secretaria.builder().id(UUID.randomUUID()).nome("Educação").email("edu@org.com").build())
                .build();

        when(responsavelRepository.findById(id)).thenReturn(Optional.of(responsavel));
        when(projetoRepository.existsByResponsaveisContains(responsavel)).thenReturn(true);

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> responsavelService.deletarResponsavel(id));

        assertTrue(ex.getMessage().contains("responsável vinculado a projetos"));
        verify(responsavelRepository, never()).deleteById(id);
    }

    @Test
    void deveListarTodosResponsaveis() {
        Responsavel r1 = Responsavel.builder().id(UUID.randomUUID()).nome("Ana").email("ana@teste.com").cargo("Dev").build();
        Responsavel r2 = Responsavel.builder().id(UUID.randomUUID()).nome("Carlos").email("carlos@teste.com").cargo("Gestor").build();

        Pageable pageable = PageRequest.of(0, 10);
        when(responsavelRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(r1, r2)));

        Page<Responsavel> pagina = responsavelService.listarTodos(pageable);

        assertEquals(2, pagina.getTotalElements());
        assertEquals("Ana", pagina.getContent().get(0).getNome());
    }


    @Test
    void deveListarPorCargo() {
        Responsavel r1 = Responsavel.builder().id(UUID.randomUUID()).nome("Ana").email("ana@teste.com").cargo("Dev").build();

        Pageable pageable = PageRequest.of(0, 10);
        when(responsavelRepository.findByCargo("Dev", pageable)).thenReturn(new PageImpl<>(List.of(r1)));

        Page<Responsavel> pagina = responsavelService.listarPorCargo("Dev", pageable);

        assertEquals(1, pagina.getTotalElements());
        assertEquals("Dev", pagina.getContent().get(0).getCargo());
    }

}
