package com.desafioacthildebertomelo.desafioacthildebertomelo.controllers;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.desafioacthildebertomelo.desafioacthildebertomelo.models.Projeto;
import com.desafioacthildebertomelo.desafioacthildebertomelo.models.StatusProjeto;
import com.desafioacthildebertomelo.desafioacthildebertomelo.services.ProjetoService;

import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/projetos")
@RequiredArgsConstructor
public class ProjetoController {
    private final ProjetoService projetoService;

    @PostMapping
    public ResponseEntity<Projeto> criar(@Valid @RequestBody Projeto projeto) {
        // Service deve recalcular status, diasAtraso e percentualTempoRestante
        return ResponseEntity.ok(projetoService.criarProjeto(projeto));
    }

    @GetMapping
    public ResponseEntity<List<Projeto>> listar() {
        return ResponseEntity.ok(projetoService.listarTodos());
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
}

