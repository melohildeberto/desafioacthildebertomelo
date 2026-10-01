package com.desafioacthildebertomelo.desafioacthildebertomelo.repositories;

import com.desafioacthildebertomelo.desafioacthildebertomelo.models.Responsavel;
import com.desafioacthildebertomelo.desafioacthildebertomelo.models.Secretaria;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ResponsavelRepository extends JpaRepository<Responsavel, UUID> {

   // Verifica se já existe um responsável com o e-mail informado
    boolean existsByEmail(String email);

    // Busca responsável por e-mail
    Optional<Responsavel> findByEmail(String email);

    // Busca todos os responsáveis por cargo
    Page<Responsavel> findByCargo(String cargo, Pageable pageable);

    // Novo método para validar vínculos
    boolean existsBySecretaria(Secretaria secretaria);
}
