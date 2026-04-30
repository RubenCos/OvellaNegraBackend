package com.costasruben.ovellanegra.repository;

import com.costasruben.ovellanegra.entities.LineaComanda;
import com.costasruben.ovellanegra.enums.EstadoLinea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LineaComandaRepository extends JpaRepository<LineaComanda, Long> {

    List<LineaComanda> findByComandaId(Long comandaId);

    /**
     * Comprueba si todas las líneas de una comanda están en un estado dado.
     * Usado para derivar automáticamente el estado global de la comanda.
     */
    @Query("""
        SELECT COUNT(l) = 0 FROM LineaComanda l
        WHERE l.comanda.id = :comandaId
        AND l.estado != :estado
        """)
    boolean allLineasInEstado(@Param("comandaId") Long comandaId,
                              @Param("estado") EstadoLinea estado);

    /**
     * Comprueba si al menos una línea tiene el estado dado.
     */
    @Query("""
        SELECT COUNT(l) > 0 FROM LineaComanda l
        WHERE l.comanda.id = :comandaId
        AND l.estado = :estado
        """)
    boolean anyLineaInEstado(@Param("comandaId") Long comandaId,
                             @Param("estado") EstadoLinea estado);
}
