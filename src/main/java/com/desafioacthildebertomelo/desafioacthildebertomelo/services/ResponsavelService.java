package com.desafioacthildebertomelo.desafioacthildebertomelo.services;

import com.desafioacthildebertomelo.desafioacthildebertomelo.models.Responsavel;
import com.desafioacthildebertomelo.desafioacthildebertomelo.repositories.ProjetoRepository;
import com.desafioacthildebertomelo.desafioacthildebertomelo.repositories.ResponsavelRepository;
import com.desafioacthildebertomelo.desafioacthildebertomelo.repositories.SecretariaRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class ResponsavelService {

    private final ResponsavelRepository responsavelRepository;
    private final SecretariaRepository secretariaRepository;
    private final ProjetoRepository projetoRepository; // novo

    public ResponsavelService(ResponsavelRepository responsavelRepository,
                              SecretariaRepository secretariaRepository,
                              ProjetoRepository projetoRepository) {
        this.responsavelRepository = responsavelRepository;
        this.secretariaRepository = secretariaRepository;
        this.projetoRepository = projetoRepository;
    }

    // ------------------- CRUD -------------------

    @Transactional
    public Responsavel criarResponsavel(Responsavel responsavel) {
        if (responsavel.getNome() == null || responsavel.getNome().isBlank()
                || responsavel.getCargo() == null || responsavel.getCargo().isBlank()) {
            throw new IllegalArgumentException("Nome e cargo são obrigatórios");
        }

        if (responsavelRepository.existsByEmail(responsavel.getEmail())) {
            throw new IllegalStateException("E-mail já cadastrado: " + responsavel.getEmail());
        }

        // Validação da secretaria
        UUID secretariaId = responsavel.getSecretaria().getId();
        secretariaRepository.findById(secretariaId)
                .orElseThrow(() -> new IllegalArgumentException("Secretaria não encontrada"));

        return responsavelRepository.save(responsavel);
    }

    @Transactional
    public Responsavel atualizarResponsavel(UUID id, Responsavel responsavelAtualizado) {
        Responsavel responsavel = responsavelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Responsável não encontrado"));

        if (!responsavel.getEmail().equals(responsavelAtualizado.getEmail())
                && responsavelRepository.existsByEmail(responsavelAtualizado.getEmail())) {
            throw new IllegalStateException("E-mail já cadastrado: " + responsavelAtualizado.getEmail());
        }

        // Validação da secretaria
        UUID secretariaId = responsavelAtualizado.getSecretaria().getId();
        secretariaRepository.findById(secretariaId)
                .orElseThrow(() -> new IllegalArgumentException("Secretaria não encontrada"));

        responsavel.setNome(responsavelAtualizado.getNome());
        responsavel.setEmail(responsavelAtualizado.getEmail());
        responsavel.setCargo(responsavelAtualizado.getCargo());
        responsavel.setSecretaria(responsavelAtualizado.getSecretaria());

        return responsavelRepository.save(responsavel);
    }

    @Transactional(readOnly = true)
    public Optional<Responsavel> buscarPorId(UUID id) {
        return responsavelRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Responsavel> buscarPorEmail(String email) {
        return responsavelRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public Page<Responsavel> listarTodos(Pageable pageable) {
        return responsavelRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Responsavel> listarPorCargo(String cargo, Pageable pageable) {
        return responsavelRepository.findByCargo(cargo, pageable);
    }

    @Transactional
    public void deletarResponsavel(UUID id) {
        Responsavel responsavel = responsavelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Responsável não encontrado"));

        // Validação: verificar se está vinculado a algum projeto
        boolean vinculado = projetoRepository.existsByResponsaveisContains(responsavel);
        if (vinculado) {
            throw new IllegalStateException("Não é possível deletar: responsável vinculado a projetos");
        }

        responsavelRepository.deleteById(id);
    }
}
