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

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SecretariaServiceTest {

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
        Secretaria secretaria = Secretaria.builder()
                .id(secretariaId)
                .nome("Secretaria de TI")
                .email("ti@org.com")
                .build();

        Responsavel novo = Responsavel.builder()
                .nome("João Silva")
                .email("joao@teste.com")
                .cargo("Analista")
                .secretaria(secretaria)
                .build();

        when(secretariaRepository.findById(secretariaId)).thenReturn(Optional.of(secretaria));
        when(responsavelRepository.existsByEmail("joao@teste.com")).thenReturn(false);
        when(responsavelRepository.save(any(Responsavel.class))).thenAnswer(inv -> inv.getArgument(0));

        Responsavel salvo = responsavelService.criarResponsavel(novo);

        assertNotNull(salvo);
        assertEquals("joao@teste.com", salvo.getEmail());
        assertEquals(secretariaId, salvo.getSecretaria().getId());
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
    void deveAtualizarResponsavelComSecretariaValida() {
        UUID id = UUID.randomUUID();
        UUID secretariaId = UUID.randomUUID();

        Secretaria secretaria = Secretaria.builder()
                .id(secretariaId)
                .nome("Secretaria de Educação")
                .email("edu@org.com")
                .build();

        Responsavel existente = Responsavel.builder()
                .id(id)
                .nome("Carlos")
                .email("carlos@teste.com")
                .cargo("Dev")
                .secretaria(secretaria)
                .build();

        Responsavel atualizado = Responsavel.builder()
                .id(id)
                .nome("Carlos Atualizado")
                .email("carlos.novo@teste.com")
                .cargo("Dev Senior")
                .secretaria(secretaria)
                .build();

        when(responsavelRepository.findById(id)).thenReturn(Optional.of(existente));
        when(secretariaRepository.findById(secretariaId)).thenReturn(Optional.of(secretaria));
        when(responsavelRepository.existsByEmail("carlos.novo@teste.com")).thenReturn(false);
        when(responsavelRepository.save(any(Responsavel.class))).thenReturn(atualizado);

        Responsavel salvo = responsavelService.atualizarResponsavel(id, atualizado);

        assertEquals("carlos.novo@teste.com", salvo.getEmail());
        assertEquals("Carlos Atualizado", salvo.getNome());
        assertEquals(secretariaId, salvo.getSecretaria().getId());
        verify(responsavelRepository, times(1)).save(any(Responsavel.class));
    }

    @Test
    void deveLancarErroAoAtualizarResponsavelComSecretariaInexistente() {
        UUID id = UUID.randomUUID();
        UUID secretariaId = UUID.randomUUID();

        Secretaria secretaria = Secretaria.builder().id(secretariaId).build();

        Responsavel existente = Responsavel.builder()
                .id(id)
                .nome("Carlos")
                .email("carlos@teste.com")
                .cargo("Dev")
                .secretaria(secretaria)
                .build();

        Responsavel atualizado = Responsavel.builder()
                .id(id)
                .nome("Carlos Atualizado")
                .email("carlos.novo@teste.com")
                .cargo("Dev Senior")
                .secretaria(secretaria)
                .build();

        when(responsavelRepository.findById(id)).thenReturn(Optional.of(existente));
        when(secretariaRepository.findById(secretariaId)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> responsavelService.atualizarResponsavel(id, atualizado));

        assertTrue(ex.getMessage().contains("Secretaria não encontrada"));
        verify(responsavelRepository, never()).save(any(Responsavel.class));
    }
}
