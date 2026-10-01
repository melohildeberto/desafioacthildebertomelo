package com.desafioacthildebertomelo.desafioacthildebertomelo.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.desafioacthildebertomelo.desafioacthildebertomelo.models.Projeto;
import com.desafioacthildebertomelo.desafioacthildebertomelo.models.StatusProjeto;
import com.desafioacthildebertomelo.desafioacthildebertomelo.services.ProjetoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/projetos")
@RequiredArgsConstructor
public class ProjetoController {

    private final ProjetoService projetoService;

    // ------------------- CRUD -------------------

    @PostMapping
    public ResponseEntity<Projeto> criar(@Valid @RequestBody Projeto projeto) {
        return ResponseEntity.ok(projetoService.criarProjeto(projeto));
    }

    @GetMapping
    public ResponseEntity<Page<Projeto>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nome") String sort) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<Projeto> projetos = projetoService.listarTodos(pageable);
        return ResponseEntity.ok(projetos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Projeto> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(projetoService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Projeto> atualizar(@PathVariable UUID id,
                                             @Valid @RequestBody Projeto projeto) {
        return ResponseEntity.ok(projetoService.atualizarProjeto(id, projeto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        projetoService.deletarProjeto(id);
        return ResponseEntity.noContent().build();
    }

    // ------------------- Kanban -------------------

    // Listar projetos por status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Projeto>> listarPorStatus(@PathVariable StatusProjeto status) {
        return ResponseEntity.ok(projetoService.listarPorStatus(status));
    }

    // Mudar status de um projeto
    @PutMapping("/{id}/status")
    public ResponseEntity<Projeto> mudarStatus(@PathVariable UUID id,
                                               @RequestParam StatusProjeto novoStatus) {
        return ResponseEntity.ok(projetoService.mudarStatus(id, novoStatus));
    }
}
