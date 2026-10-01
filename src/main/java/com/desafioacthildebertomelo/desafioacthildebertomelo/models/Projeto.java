package com.desafioacthildebertomelo.desafioacthildebertomelo.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.GeneratedValue;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Projeto {

    @Id
    @GeneratedValue
    private UUID id;

    @NotNull(message = "Nome do projeto é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String nome;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status é obrigatório")
    private StatusProjeto status;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "projeto_responsavel",
        joinColumns = @JoinColumn(name = "projeto_id"),
        inverseJoinColumns = @JoinColumn(name = "responsavel_id")
    )
    private Set<Responsavel> responsaveis;

    @NotNull(message = "Data de início previsto é obrigatória")
    private LocalDate inicioPrevisto;

    @NotNull(message = "Data de término previsto é obrigatória")
    private LocalDate terminoPrevisto;

    private LocalDate inicioRealizado;
    private LocalDate terminoRealizado;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Calculado dinamicamente
    public long getDiasDeAtraso() {
        if (terminoRealizado != null && terminoPrevisto != null) {
            return terminoRealizado.isAfter(terminoPrevisto)
                    ? terminoPrevisto.until(terminoRealizado).getDays()
                    : 0;
        }
        return 0;
    }

    public double getPercentualTempoRestante() {
        if (inicioPrevisto != null && terminoPrevisto != null) {
            long diasTotais = inicioPrevisto.until(terminoPrevisto).getDays();
            long diasRestantes = LocalDate.now().until(terminoPrevisto).getDays();
            return diasTotais > 0 ? (diasRestantes * 100.0) / diasTotais : 0;
        }
        return 0;
    }
}
