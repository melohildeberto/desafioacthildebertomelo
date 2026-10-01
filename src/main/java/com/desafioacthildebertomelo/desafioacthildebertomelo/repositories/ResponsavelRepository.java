package com.desafioacthildebertomelo.desafioacthildebertomelo.repositories;

import com.desafioacthildebertomelo.desafioacthildebertomelo.models.Responsavel;
import com.desafioacthildebertomelo.desafioacthildebertomelo.models.Secretaria;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ResponsavelRepository extends JpaRepository<Responsavel, UUID> {

   // Verifica se já existe um responsável com o e-mail informado
    boolean existsByEmail(String email);

    // Busca responsável por e-mail
    Optional<Responsavel> findByEmail(String email);

    // Busca todos os responsáveis por cargo
    List<Responsavel> findByCargo(String cargo);

    // Novo método para validar vínculos
    boolean existsBySecretaria(Secretaria secretaria);
}
