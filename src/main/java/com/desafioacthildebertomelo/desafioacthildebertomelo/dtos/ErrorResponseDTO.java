package com.desafioacthildebertomelo.desafioacthildebertomelo.dtos;

import java.time.Instant;
import java.util.List;

public record ErrorResponseDTO(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        List<FieldErrorDTO> fieldErrors // Opcional: para erros de validação de formulário (@Valid)
) {
    public record FieldErrorDTO(String field, String message) {}
}
