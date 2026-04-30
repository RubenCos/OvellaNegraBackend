package com.costasruben.ovellanegra.repository;

import com.costasruben.ovellanegra.entities.PlatoIngrediente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlatoIngredienteRepository extends JpaRepository<PlatoIngrediente, Long> {
    List<PlatoIngrediente> findByPlatoId(Long platoId);
}
