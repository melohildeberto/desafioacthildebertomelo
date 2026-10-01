package com.desafioacthildebertomelo.desafioacthildebertomelo.controllers;

import com.desafioacthildebertomelo.desafioacthildebertomelo.models.Secretaria;
import com.desafioacthildebertomelo.desafioacthildebertomelo.services.SecretariaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
    public ResponseEntity<List<Secretaria>> listarTodas() {
        return ResponseEntity.ok(secretariaService.listarTodas());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        secretariaService.deletarSecretaria(id);
        return ResponseEntity.noContent().build();
    }
}
