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

    // Listar todos os projetos
    @Transactional(readOnly = true)
    public List<Projeto> listarTodos() {
        return projetoRepository.findAll();
    }

    // Criar novo projeto
    @Transactional
    public Projeto criarProjeto(Projeto projeto) {
        atualizarStatus(projeto);
        return projetoRepository.save(projeto);
    }

    // Atualizar projeto existente
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

    // Buscar projeto por ID
    @Transactional(readOnly = true)
    public Projeto buscarPorId(UUID id) {
        return projetoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Projeto não encontrado"));
    }

    // Deletar projeto
    @Transactional
    public void deletarProjeto(UUID id) {
        if (!projetoRepository.existsById(id)) {
            throw new IllegalArgumentException("Projeto não encontrado");
        }
        projetoRepository.deleteById(id);
    }

    // Recalcular status automaticamente
    public void atualizarStatus(Projeto projeto) {
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
}
