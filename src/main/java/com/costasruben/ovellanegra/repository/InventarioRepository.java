package com.costasruben.ovellanegra.repository;

import com.costasruben.ovellanegra.entities.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {
    Optional<Inventario> findByNombreIngrediente(String nombre);

    /** Ingredientes con stock por debajo del mínimo */
    @Query("SELECT i FROM Inventario i WHERE i.cantidad < i.stockMinimo")
    List<Inventario> findStockBajo();
}