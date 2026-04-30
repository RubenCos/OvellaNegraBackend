package com.costasruben.ovellanegra.repository;

import com.costasruben.ovellanegra.entities.Plato;
import com.costasruben.ovellanegra.enums.CategoriaPlato;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlatoRepository extends JpaRepository<Plato, Long> {
    List<Plato> findByCategoria(CategoriaPlato categoria);
    List<Plato> findByDisponibleTrue();
    List<Plato> findByCategoriaAndDisponibleTrue(CategoriaPlato categoria);
}