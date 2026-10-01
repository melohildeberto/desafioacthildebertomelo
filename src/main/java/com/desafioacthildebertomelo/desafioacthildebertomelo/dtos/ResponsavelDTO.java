package com.desafioacthildebertomelo.desafioacthildebertomelo.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponsavelDTO {

    private UUID id;

    @NotNull(message = "Nome é obrigatório")
    @Size(min = 3, max = 100)
    private String nome;

    @NotNull(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    private String email;

    @NotNull(message = "Cargo é obrigatório")
    private String cargo;

    private UUID secretariaId;
}
