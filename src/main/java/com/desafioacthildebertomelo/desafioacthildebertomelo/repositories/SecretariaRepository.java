package com.desafioacthildebertomelo.desafioacthildebertomelo.repositories;

import com.desafioacthildebertomelo.desafioacthildebertomelo.models.Secretaria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SecretariaRepository extends JpaRepository<Secretaria, UUID> {

    // Verifica se já existe uma secretaria com o e-mail informado
    boolean existsByEmail(String email);

    // Busca secretaria por e-mail
    Optional<Secretaria> findByEmail(String email);
}
