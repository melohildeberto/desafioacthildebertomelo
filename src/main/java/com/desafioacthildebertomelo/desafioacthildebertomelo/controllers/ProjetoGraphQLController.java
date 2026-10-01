package com.desafioacthildebertomelo.desafioacthildebertomelo.controllers;

import com.desafioacthildebertomelo.desafioacthildebertomelo.models.Projeto;
import com.desafioacthildebertomelo.desafioacthildebertomelo.models.Responsavel;
import com.desafioacthildebertomelo.desafioacthildebertomelo.services.ProjetoService;
import com.desafioacthildebertomelo.desafioacthildebertomelo.services.ResponsavelService;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.data.domain.PageRequest;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Controller
public class ProjetoGraphQLController {

    private final ProjetoService projetoService;
    private final ResponsavelService responsavelService;

    public ProjetoGraphQLController(ProjetoService projetoService, ResponsavelService responsavelService) {
        this.projetoService = projetoService;
        this.responsavelService = responsavelService;
    }

    @QueryMapping
    public Page<Projeto> projetos(@Argument int page, @Argument int size) {
        Pageable pageable = PageRequest.of(page, size);
        return projetoService.listarTodos(pageable);
    }

    @QueryMapping
    public Projeto projetoPorId(@Argument UUID id) {
        return projetoService.buscarPorId(id);
    }

    @QueryMapping
     public Page<Responsavel> responsaveis(@Argument int page, @Argument int size) {
        Pageable pageable = PageRequest.of(page, size);
        return responsavelService.listarTodos(pageable);
    }

    @QueryMapping
    public Responsavel responsavelPorEmail(@Argument String email) {
        return responsavelService.buscarPorEmail(email).orElse(null);
    }

    @MutationMapping
    public Projeto criarProjeto(@Argument String nome,
                                @Argument String inicioPrevisto,
                                @Argument String terminoPrevisto) {
        Projeto projeto = Projeto.builder()
                .nome(nome)
                .inicioPrevisto(LocalDate.parse(inicioPrevisto))
                .terminoPrevisto(LocalDate.parse(terminoPrevisto))
                .build();
        return projetoService.criarProjeto(projeto);
    }

    @MutationMapping
    public Projeto atualizarProjeto(@Argument UUID id,
                                    @Argument String nome,
                                    @Argument String inicioPrevisto,
                                    @Argument String terminoPrevisto) {
        Projeto projeto = Projeto.builder()
                .nome(nome)
                .inicioPrevisto(inicioPrevisto != null ? LocalDate.parse(inicioPrevisto) : null)
                .terminoPrevisto(terminoPrevisto != null ? LocalDate.parse(terminoPrevisto) : null)
                .build();
        return projetoService.atualizarProjeto(id, projeto);
    }

    @MutationMapping
    public Boolean deletarProjeto(@Argument UUID id) {
        projetoService.deletarProjeto(id);
        return true;
    }

    @MutationMapping
    public Responsavel criarResponsavel(@Argument String nome,
                                        @Argument String email,
                                        @Argument String cargo) {
        Responsavel r = Responsavel.builder().nome(nome).email(email).cargo(cargo).build();
        return responsavelService.criarResponsavel(r);
    }

    @MutationMapping
    public Responsavel atualizarResponsavel(@Argument UUID id,
                                            @Argument String nome,
                                            @Argument String email,
                                            @Argument String cargo) {
        Responsavel r = Responsavel.builder().nome(nome).email(email).cargo(cargo).build();
        return responsavelService.atualizarResponsavel(id, r);
    }

    @MutationMapping
    public Boolean deletarResponsavel(@Argument UUID id) {
        responsavelService.deletarResponsavel(id);
        return true;
    }
}
