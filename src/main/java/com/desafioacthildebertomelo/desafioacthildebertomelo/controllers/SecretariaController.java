package com.desafioacthildebertomelo.desafioacthildebertomelo.controllers;

import com.desafioacthildebertomelo.desafioacthildebertomelo.models.Secretaria;
import com.desafioacthildebertomelo.desafioacthildebertomelo.services.SecretariaService;
import jakarta.validation.Valid;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/api/secretarias")
public class SecretariaController {

    private final SecretariaService secretariaService;

    public SecretariaController(SecretariaService secretariaService) {
        this.secretariaService = secretariaService;
    }

    @PostMapping
    public ResponseEntity<Secretaria> criar(@Valid @RequestBody Secretaria secretaria) {
        return ResponseEntity.ok(secretariaService.criarSecretaria(secretaria));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Secretaria> atualizar(@PathVariable UUID id,
                                                @Valid @RequestBody Secretaria secretariaAtualizada) {
        return ResponseEntity.ok(secretariaService.atualizarSecretaria(id, secretariaAtualizada));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Secretaria> buscarPorId(@PathVariable UUID id) {
        return secretariaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Secretaria> buscarPorEmail(@PathVariable String email) {
        return secretariaService.buscarPorEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<Secretaria>> listarTodas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nome") String sort) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return ResponseEntity.ok(secretariaService.listarTodas(pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        secretariaService.deletarSecretaria(id);
        return ResponseEntity.noContent().build();
    }
}
