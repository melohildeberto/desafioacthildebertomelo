package com.desafioacthildebertomelo.desafioacthildebertomelo.services;

import com.desafioacthildebertomelo.desafioacthildebertomelo.models.Secretaria;
import com.desafioacthildebertomelo.desafioacthildebertomelo.repositories.ResponsavelRepository;
import com.desafioacthildebertomelo.desafioacthildebertomelo.repositories.SecretariaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class SecretariaService {

    private final SecretariaRepository secretariaRepository;
    private final ResponsavelRepository responsavelRepository;

    public SecretariaService(SecretariaRepository secretariaRepository,
                             ResponsavelRepository responsavelRepository) {
        this.secretariaRepository = secretariaRepository;
        this.responsavelRepository = responsavelRepository;
    }

    // ------------------- CRUD -------------------

    @Transactional
    public Secretaria criarSecretaria(Secretaria secretaria) {
        if (secretaria.getNome() == null || secretaria.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome da secretaria é obrigatório");
        }
        if (secretaria.getEmail() == null || secretaria.getEmail().isBlank()) {
            throw new IllegalArgumentException("E-mail da secretaria é obrigatório");
        }
        if (secretariaRepository.existsByEmail(secretaria.getEmail())) {
            throw new IllegalStateException("E-mail já cadastrado: " + secretaria.getEmail());
        }
        return secretariaRepository.save(secretaria);
    }

    @Transactional
    public Secretaria atualizarSecretaria(UUID id, Secretaria secretariaAtualizada) {
        Secretaria secretaria = secretariaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Secretaria não encontrada"));

        if (!secretaria.getEmail().equals(secretariaAtualizada.getEmail())
                && secretariaRepository.existsByEmail(secretariaAtualizada.getEmail())) {
            throw new IllegalStateException("E-mail já cadastrado: " + secretariaAtualizada.getEmail());
        }

        secretaria.setNome(secretariaAtualizada.getNome());
        secretaria.setEmail(secretariaAtualizada.getEmail());
        secretaria.setTelefone(secretariaAtualizada.getTelefone());

        return secretariaRepository.save(secretaria);
    }

    @Transactional(readOnly = true)
    public Optional<Secretaria> buscarPorId(UUID id) {
        return secretariaRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Secretaria> buscarPorEmail(String email) {
        return secretariaRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public Page<Secretaria> listarTodas(Pageable pageable) {
        return secretariaRepository.findAll(pageable);
    }

    @Transactional
    public void deletarSecretaria(UUID id) {
        Secretaria secretaria = secretariaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Secretaria não encontrada"));

        // Validação: verificar vínculos com responsáveis
        boolean possuiResponsaveis = responsavelRepository.existsBySecretaria(secretaria);
        if (possuiResponsaveis) {
            throw new IllegalStateException("Não é possível deletar: secretaria possui responsáveis vinculados");
        }

        secretariaRepository.deleteById(id);
    }
}
