package com.desafioacthildebertomelo.desafioacthildebertomelo.repositories;

import com.desafioacthildebertomelo.desafioacthildebertomelo.models.Responsavel;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ResponsavelRepository extends JpaRepository<Responsavel, UUID> {

   // Verifica se já existe um responsável com o e-mail informado
    boolean existsByEmail(String email);
}
