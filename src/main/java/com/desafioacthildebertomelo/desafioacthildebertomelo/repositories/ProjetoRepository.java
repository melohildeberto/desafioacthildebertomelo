package com.desafioacthildebertomelo.desafioacthildebertomelo.repositories;

import com.desafioacthildebertomelo.desafioacthildebertomelo.models.Projeto;
import com.desafioacthildebertomelo.desafioacthildebertomelo.models.StatusProjeto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProjetoRepository extends JpaRepository<Projeto, UUID> {

    List<Projeto> findByStatus(StatusProjeto status);
}
