package com.desafioacthildebertomelo.desafioacthildebertomelo.controllers;

import com.desafioacthildebertomelo.desafioacthildebertomelo.models.Projeto;
import com.desafioacthildebertomelo.desafioacthildebertomelo.models.Responsavel;
import com.desafioacthildebertomelo.desafioacthildebertomelo.services.ProjetoService;
import com.desafioacthildebertomelo.desafioacthildebertomelo.services.ResponsavelService;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Controller
public class ProjetoGraphQLController {

    private final ProjetoService projetoService;
    private final ResponsavelService responsavelService;

    public ProjetoGraphQLController(ProjetoService projetoService, ResponsavelService responsavelService) {
        this.projetoService = projetoService;
        this.responsavelService = responsavelService;
    }

    @QueryMapping
    public List<Projeto> projetos() {
        return projetoService.listarTodos();
    }

    @QueryMapping
    public Projeto projetoPorId(@Argument UUID id) {
        return projetoService.buscarPorId(id);
    }

    @QueryMapping
    public List<Responsavel> responsaveis() {
        return responsavelService.listarTodos();
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
