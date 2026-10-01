package com.desafioacthildebertomelo.desafioacthildebertomelo.dtos;

import com.desafioacthildebertomelo.desafioacthildebertomelo.models.StatusProjeto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjetoDTO {

    private UUID id;

    @NotNull(message = "Nome do projeto é obrigatório")
    @Size(min = 3, max = 100)
    private String nome;

    @NotNull(message = "Status é obrigatório")
    private StatusProjeto status;

    @NotNull(message = "Data de início previsto é obrigatória")
    private LocalDate inicioPrevisto;

    @NotNull(message = "Data de término previsto é obrigatória")
    private LocalDate terminoPrevisto;

    private LocalDate inicioRealizado;
    private LocalDate terminoRealizado;

    private Set<UUID> responsaveisIds;
}
