package com.costasruben.ovellanegra.repository;

import com.costasruben.ovellanegra.entities.PlatoIngrediente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlatoIngredienteRepository extends JpaRepository<PlatoIngrediente, Long> {

    /**
     * Trae los ingredientes de un plato con el Inventario ya cargado (EAGER).
     * Evita LazyInitializationException en InventarioServiceImpl.descontarStock()
     */
    @Query("""
        SELECT pi FROM PlatoIngrediente pi
        JOIN FETCH pi.ingrediente
        WHERE pi.plato.id = :platoId
        """)
    List<PlatoIngrediente> findByPlatoIdWithIngrediente(@Param("platoId") Long platoId);

    List<PlatoIngrediente> findByPlatoId(Long platoId);
}
