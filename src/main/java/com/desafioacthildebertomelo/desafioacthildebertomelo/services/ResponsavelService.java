package com.desafioacthildebertomelo.desafioacthildebertomelo.services;

import com.desafioacthildebertomelo.desafioacthildebertomelo.models.Responsavel;
import com.desafioacthildebertomelo.desafioacthildebertomelo.repositories.ResponsavelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ResponsavelService {

    private final ResponsavelRepository responsavelRepository;

    public ResponsavelService(ResponsavelRepository responsavelRepository) {
        this.responsavelRepository = responsavelRepository;
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

        return responsavelRepository.save(responsavel);
    }

    @Transactional
    public Responsavel atualizarResponsavel(UUID id, Responsavel responsavelAtualizado) {
        Responsavel responsavel = responsavelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Responsável não encontrado"));

        // Se o e-mail foi alterado, validar duplicidade
        if (!responsavel.getEmail().equals(responsavelAtualizado.getEmail())
                && responsavelRepository.existsByEmail(responsavelAtualizado.getEmail())) {
            throw new IllegalStateException("E-mail já cadastrado: " + responsavelAtualizado.getEmail());
        }

        responsavel.setNome(responsavelAtualizado.getNome());
        responsavel.setEmail(responsavelAtualizado.getEmail());
        responsavel.setCargo(responsavelAtualizado.getCargo());

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
    public List<Responsavel> listarTodos() {
        return responsavelRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Responsavel> listarPorCargo(String cargo) {
        return responsavelRepository.findByCargo(cargo);
    }

    @Transactional
    public void deletarResponsavel(UUID id) {
        if (!responsavelRepository.existsById(id)) {
            throw new IllegalArgumentException("Responsável não encontrado");
        }
        responsavelRepository.deleteById(id);
    }
}
