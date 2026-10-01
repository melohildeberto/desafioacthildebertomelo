package com.desafioacthildebertomelo.desafioacthildebertomelo.controllers;

import com.desafioacthildebertomelo.desafioacthildebertomelo.models.Responsavel;
import com.desafioacthildebertomelo.desafioacthildebertomelo.services.ResponsavelService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/api/responsaveis")
public class ResponsavelController {

    private final ResponsavelService responsavelService;

    public ResponsavelController(ResponsavelService responsavelService) {
        this.responsavelService = responsavelService;
    }

    // Criar responsável
    @PostMapping
    public ResponseEntity<Responsavel> criar(@Valid @RequestBody Responsavel responsavel) {
        return ResponseEntity.ok(responsavelService.criarResponsavel(responsavel));
    }

    // Atualizar responsável
    @PutMapping("/{id}")
    public ResponseEntity<Responsavel> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody Responsavel responsavelAtualizado) {
        return ResponseEntity.ok(responsavelService.atualizarResponsavel(id, responsavelAtualizado));
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Responsavel> buscarPorId(@PathVariable UUID id) {
        return responsavelService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Buscar por email
    @GetMapping("/email/{email}")
    public ResponseEntity<Responsavel> buscarPorEmail(@PathVariable String email) {
        return responsavelService.buscarPorEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Listar todos
    @GetMapping
    public ResponseEntity<Page<Responsavel>> listarTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nome") String sort) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return ResponseEntity.ok(responsavelService.listarTodos(pageable));
    }

    // Listar por cargo
    @GetMapping("/cargo/{cargo}")
    public ResponseEntity<Page<Responsavel>> listarPorCargo(
            @PathVariable String cargo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nome") String sort) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return ResponseEntity.ok(responsavelService.listarPorCargo(cargo, pageable));
    }

    // Deletar responsável
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        responsavelService.deletarResponsavel(id);
        return ResponseEntity.noContent().build();
    }
}
