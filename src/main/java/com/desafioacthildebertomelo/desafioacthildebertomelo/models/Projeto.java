package com.desafioacthildebertomelo.desafioacthildebertomelo.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.*;
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
@Table(
    name = "projeto",
    indexes = {
        @Index(name = "idx_projeto_status", columnList = "status")
    }
)
public class Projeto {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotNull(message = "Nome do projeto é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status é obrigatório")
    @Column(name = "status", nullable = false, length = 30)
    private StatusProjeto status;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "projeto_responsavel",
        joinColumns = @JoinColumn(name = "projeto_id"),
        inverseJoinColumns = @JoinColumn(name = "responsavel_id")
    )
    private Set<Responsavel> responsaveis;

    @NotNull(message = "Data de início previsto é obrigatória")
    @Column(name = "inicio_previsto", nullable = false)
    private LocalDate inicioPrevisto;

    @NotNull(message = "Data de término previsto é obrigatória")
    @Column(name = "termino_previsto", nullable = false)
    private LocalDate terminoPrevisto;

    @Column(name = "inicio_realizado")
    private LocalDate inicioRealizado;

    @Column(name = "termino_realizado")
    private LocalDate terminoRealizado;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Calculado dinamicamente
    public long getDiasDeAtraso() {
        if (terminoRealizado != null && terminoPrevisto != null) {
            return terminoRealizado.isAfter(terminoPrevisto)
                    ? ChronoUnit.DAYS.between(terminoPrevisto, terminoRealizado)
                    : 0;
        }
        return 0;
    }

    public double getPercentualTempoRestante() {
        if (inicioPrevisto != null && terminoPrevisto != null) {
            long diasTotais = ChronoUnit.DAYS.between(inicioPrevisto, terminoPrevisto);
            long diasRestantes = ChronoUnit.DAYS.between(LocalDate.now(), terminoPrevisto);
            return diasTotais > 0 ? (diasRestantes * 100.0) / diasTotais : 0;
        }
        return 0;
    }
}
