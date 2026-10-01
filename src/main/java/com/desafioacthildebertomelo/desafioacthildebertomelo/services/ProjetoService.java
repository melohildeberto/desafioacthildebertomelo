package com.desafioacthildebertomelo.desafioacthildebertomelo.services;

import com.desafioacthildebertomelo.desafioacthildebertomelo.models.Projeto;
import com.desafioacthildebertomelo.desafioacthildebertomelo.models.StatusProjeto;
import com.desafioacthildebertomelo.desafioacthildebertomelo.repositories.ProjetoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class ProjetoService {

    private final ProjetoRepository projetoRepository;

    public ProjetoService(ProjetoRepository projetoRepository) {
        this.projetoRepository = projetoRepository;
    }

    // ------------------- CRUD -------------------

    @Transactional(readOnly = true)
    public List<Projeto> listarTodos() {
        return projetoRepository.findAll();
    }

    @Transactional
    public Projeto criarProjeto(Projeto projeto) {
        atualizarStatus(projeto);
        return projetoRepository.save(projeto);
    }

    @Transactional
    public Projeto atualizarProjeto(UUID id, Projeto projetoAtualizado) {
        Projeto projeto = projetoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Projeto não encontrado"));

        projeto.setNome(projetoAtualizado.getNome());
        projeto.setInicioPrevisto(projetoAtualizado.getInicioPrevisto());
        projeto.setTerminoPrevisto(projetoAtualizado.getTerminoPrevisto());
        projeto.setInicioRealizado(projetoAtualizado.getInicioRealizado());
        projeto.setTerminoRealizado(projetoAtualizado.getTerminoRealizado());

        atualizarStatus(projeto);
        return projetoRepository.save(projeto);
    }

    @Transactional(readOnly = true)
    public Projeto buscarPorId(UUID id) {
        return projetoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Projeto não encontrado"));
    }

    @Transactional
    public void deletarProjeto(UUID id) {
        if (!projetoRepository.existsById(id)) {
            throw new IllegalArgumentException("Projeto não encontrado");
        }
        projetoRepository.deleteById(id);
    }

    // ------------------- Kanban -------------------

    @Transactional(readOnly = true)
    public List<Projeto> listarPorStatus(StatusProjeto status) {
        return projetoRepository.findByStatus(status);
    }

    @Transactional
    public Projeto mudarStatus(UUID id, StatusProjeto novoStatus) {
        Projeto projeto = projetoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Projeto não encontrado"));

        StatusProjeto statusAtual = projeto.getStatus();

        aplicarAcoesAutomaticas(projeto, novoStatus);
        validarTransicao(projeto, statusAtual, novoStatus);

        projeto.setStatus(novoStatus);
        return projetoRepository.save(projeto);
    }

    // ------------------- Regras de negócio -------------------

    private void atualizarStatus(Projeto projeto) {
        LocalDate hoje = LocalDate.now();

        if (projeto.getTerminoRealizado() != null) {
            projeto.setStatus(StatusProjeto.CONCLUIDO);
        } else if ((projeto.getInicioPrevisto() != null && projeto.getInicioPrevisto().isBefore(hoje) && projeto.getInicioRealizado() == null)
                || (projeto.getTerminoPrevisto() != null && projeto.getTerminoPrevisto().isBefore(hoje))) {
            projeto.setStatus(StatusProjeto.ATRASADO);
        } else if (projeto.getInicioRealizado() != null) {
            projeto.setStatus(StatusProjeto.EM_ANDAMENTO);
        } else {
            projeto.setStatus(StatusProjeto.AINICIAR);
        }
    }

    private void aplicarAcoesAutomaticas(Projeto projeto, StatusProjeto novo) {
        LocalDate hoje = LocalDate.now();

        if (novo == StatusProjeto.EM_ANDAMENTO && projeto.getInicioRealizado() == null) {
            projeto.setInicioRealizado(hoje);
        }

        if (novo == StatusProjeto.CONCLUIDO && projeto.getTerminoRealizado() == null) {
            projeto.setTerminoRealizado(hoje);
        }
    }

    private void validarTransicao(Projeto projeto, StatusProjeto atual, StatusProjeto novo) {
        LocalDate hoje = LocalDate.now();

        switch (novo) {
            case EM_ANDAMENTO:
                if (projeto.getInicioRealizado() == null) {
                    throw new IllegalStateException("Não é possível iniciar sem data de início realizada");
                }
                break;
            case ATRASADO:
                boolean condicaoAtraso = (projeto.getInicioPrevisto() != null && projeto.getInicioPrevisto().isBefore(hoje) && projeto.getInicioRealizado() == null)
                        || (projeto.getTerminoPrevisto() != null && projeto.getTerminoPrevisto().isBefore(hoje) && projeto.getTerminoRealizado() == null);
                if (!condicaoAtraso) {
                    throw new IllegalStateException("Condições de atraso cronológico não atendidas");
                }
                break;
            case CONCLUIDO:
                if (projeto.getTerminoRealizado() == null) {
                    throw new IllegalStateException("Não é possível concluir sem data de término realizada");
                }
                break;
            case AINICIAR:
                if (projeto.getInicioRealizado() != null || projeto.getTerminoRealizado() != null) {
                    throw new IllegalStateException("Projeto já iniciado ou concluído, não pode voltar para 'A iniciar'");
                }
                break;
        }
    }
}
