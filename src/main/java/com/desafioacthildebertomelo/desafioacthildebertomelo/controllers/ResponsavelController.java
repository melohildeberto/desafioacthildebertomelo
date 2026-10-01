package com.desafioacthildebertomelo.desafioacthildebertomelo.controllers;

import com.desafioacthildebertomelo.desafioacthildebertomelo.models.Responsavel;
import com.desafioacthildebertomelo.desafioacthildebertomelo.services.ResponsavelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/responsaveis")
@RequiredArgsConstructor
public class ResponsavelController {

    private final ResponsavelService responsavelService;
    

    @PostMapping
    public ResponseEntity<Responsavel> criar(@Valid @RequestBody Responsavel responsavel) {
        // Service deve validar e-mail único
        return ResponseEntity.ok(responsavelService.criarResponsavel(responsavel));
    }

    @GetMapping
    public ResponseEntity<List<Responsavel>> listar() {
        return ResponseEntity.ok(responsavelService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Responsavel> buscarPorId(@PathVariable UUID id) {
        return responsavelService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Responsavel> atualizar(@PathVariable UUID id,
                                                 @Valid @RequestBody Responsavel responsavel) {
        return ResponseEntity.ok(responsavelService.atualizarResponsavel(id, responsavel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        responsavelService.deletarResponsavel(id);
        return ResponseEntity.noContent().build();
    }
}
