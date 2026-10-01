package com.desafioacthildebertomelo.desafioacthildebertomelo;

import com.desafioacthildebertomelo.desafioacthildebertomelo.models.Responsavel;
import com.desafioacthildebertomelo.desafioacthildebertomelo.repositories.ResponsavelRepository;
import com.desafioacthildebertomelo.desafioacthildebertomelo.services.ResponsavelService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ResponsavelServiceTest {

    private ResponsavelRepository responsavelRepository;
    private ResponsavelService responsavelService;

    @BeforeEach
    void setUp() {
        responsavelRepository = Mockito.mock(ResponsavelRepository.class);
        responsavelService = new ResponsavelService(responsavelRepository);
    }

    @Test
    void deveCriarResponsavelComEmailUnico() {
        Responsavel novo = Responsavel.builder()
                .nome("João Silva")
                .email("joao@teste.com")
                .cargo("Analista")
                .build();

        when(responsavelRepository.existsByEmail("joao@teste.com")).thenReturn(false);
        when(responsavelRepository.save(novo)).thenReturn(novo);

        Responsavel salvo = responsavelService.criarResponsavel(novo);

        assertNotNull(salvo);
        assertEquals("joao@teste.com", salvo.getEmail());
        verify(responsavelRepository, times(1)).save(novo);
    }

    @Test
    void deveLancarErroAoCriarResponsavelComEmailDuplicado() {
        Responsavel novo = Responsavel.builder()
                .nome("Maria")
                .email("maria@teste.com")
                .cargo("Gestora")
                .build();

        when(responsavelRepository.existsByEmail("maria@teste.com")).thenReturn(true);

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> responsavelService.criarResponsavel(novo));

        assertTrue(ex.getMessage().contains("E-mail já cadastrado"));
        verify(responsavelRepository, never()).save(novo);
    }

    @Test
    void deveAtualizarResponsavelComEmailNovoUnico() {
        UUID id = UUID.randomUUID();
        Responsavel existente = Responsavel.builder()
                .id(id)
                .nome("Carlos")
                .email("carlos@teste.com")
                .cargo("Dev")
                .build();

        Responsavel atualizado = Responsavel.builder()
                .id(id)
                .nome("Carlos Atualizado")
                .email("carlos.novo@teste.com")
                .cargo("Dev Senior")
                .build();

        when(responsavelRepository.findById(id)).thenReturn(Optional.of(existente));
        when(responsavelRepository.existsByEmail("carlos.novo@teste.com")).thenReturn(false);
        when(responsavelRepository.save(any(Responsavel.class))).thenReturn(atualizado);

        Responsavel salvo = responsavelService.atualizarResponsavel(id, atualizado);

        assertEquals("carlos.novo@teste.com", salvo.getEmail());
        assertEquals("Carlos Atualizado", salvo.getNome());
        verify(responsavelRepository, times(1)).save(any(Responsavel.class));
    }

    @Test
    void deveLancarErroAoAtualizarResponsavelComEmailDuplicado() {
        UUID id = UUID.randomUUID();
        Responsavel existente = Responsavel.builder()
                .id(id)
                .nome("Carlos")
                .email("carlos@teste.com")
                .cargo("Dev")
                .build();

        Responsavel duplicado = Responsavel.builder()
                .id(id)
                .nome("Carlos Atualizado")
                .email("maria@teste.com")
                .cargo("Dev Senior")
                .build();

        when(responsavelRepository.findById(id)).thenReturn(Optional.of(existente));
        when(responsavelRepository.existsByEmail("maria@teste.com")).thenReturn(true);

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> responsavelService.atualizarResponsavel(id, duplicado));

        assertTrue(ex.getMessage().contains("E-mail já cadastrado"));
        verify(responsavelRepository, never()).save(duplicado);
    }

    @Test
    void deveLancarErroAoAtualizarResponsavelInexistente() {
        UUID id = UUID.randomUUID();
        Responsavel atualizado = Responsavel.builder()
                .id(id)
                .nome("Teste")
                .email("teste@teste.com")
                .cargo("Dev")
                .build();

        when(responsavelRepository.findById(id)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> responsavelService.atualizarResponsavel(id, atualizado));

        assertTrue(ex.getMessage().contains("Responsável não encontrado"));
        verify(responsavelRepository, never()).save(any(Responsavel.class));
    }

    @Test
    void deveDeletarResponsavel() {
        UUID id = UUID.randomUUID();
        when(responsavelRepository.existsById(id)).thenReturn(true);
        doNothing().when(responsavelRepository).deleteById(id);

        responsavelService.deletarResponsavel(id);

        verify(responsavelRepository, times(1)).deleteById(id);
    }

    @Test
    void deveLancarErroAoDeletarResponsavelInexistente() {
        UUID id = UUID.randomUUID();
        when(responsavelRepository.existsById(id)).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> responsavelService.deletarResponsavel(id));

        assertTrue(ex.getMessage().contains("Responsável não encontrado"));
        verify(responsavelRepository, never()).deleteById(id);
    }
    
    @Test
    void deveBuscarPorEmail() {
        Responsavel responsavel = Responsavel.builder()
                .id(UUID.randomUUID())
                .nome("Ana")
                .email("ana@teste.com")
                .cargo("Coordenadora")
                .build();

        when(responsavelRepository.findByEmail("ana@teste.com")).thenReturn(Optional.of(responsavel));

        Optional<Responsavel> resultado = responsavelService.buscarPorEmail("ana@teste.com");

        assertTrue(resultado.isPresent());
        assertEquals("Ana", resultado.get().getNome());
    }

    @Test
    void deveLancarErroAoCriarResponsavelSemNomeOuCargo() {
        Responsavel invalido = Responsavel.builder()
                .id(UUID.randomUUID())
                .email("teste@teste.com")
                .build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> responsavelService.criarResponsavel(invalido));
        assertEquals("Nome e cargo são obrigatórios", ex.getMessage());
        verify(responsavelRepository, never()).save(invalido);
    }

    @Test
        void deveListarTodosResponsaveis() {
        Responsavel r1 = Responsavel.builder().id(UUID.randomUUID()).nome("Ana").email("ana@teste.com").cargo("Dev").build();
        Responsavel r2 = Responsavel.builder().id(UUID.randomUUID()).nome("Carlos").email("carlos@teste.com").cargo("Gestor").build();

        when(responsavelRepository.findAll()).thenReturn(List.of(r1, r2));

        List<Responsavel> lista = responsavelService.listarTodos();

        assertEquals(2, lista.size());
        assertEquals("Ana", lista.get(0).getNome());
        }

        @Test
        void deveListarPorCargo() {
        Responsavel r1 = Responsavel.builder().id(UUID.randomUUID()).nome("Ana").email("ana@teste.com").cargo("Dev").build();

        when(responsavelRepository.findByCargo("Dev")).thenReturn(List.of(r1));

        List<Responsavel> lista = responsavelService.listarPorCargo("Dev");

        assertEquals(1, lista.size());
        assertEquals("Dev", lista.get(0).getCargo());
        }

}
