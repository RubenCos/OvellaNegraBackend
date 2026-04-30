package com.costasruben.ovellanegra.repository;

import com.costasruben.ovellanegra.entities.Comanda;
import com.costasruben.ovellanegra.enums.EstadoComanda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ComandaRepository extends JpaRepository<Comanda, Long> {

    List<Comanda> findByMesaId(Long mesaId);
    List<Comanda> findByEstado(EstadoComanda estado);

    /**
     * Comandas activas para la pantalla de cocina.
     * JOIN FETCH garantiza que lineas y plato se cargan en la misma query
     * evitando LazyInitializationException al serializar.
     */
    @Query("""
        SELECT DISTINCT c FROM Comanda c
        LEFT JOIN FETCH c.lineas l
        LEFT JOIN FETCH l.plato
        WHERE c.estado IN ('PENDIENTE','EN_PREPARACION')
        ORDER BY c.fechaHora ASC
        """)
    List<Comanda> findComandasActivas();

    /**
     * Comanda por id con todas sus líneas y platos cargados.
     * Usada en cambiarEstado() y descontarStockPorComanda().
     */
    @Query("""
        SELECT DISTINCT c FROM Comanda c
        LEFT JOIN FETCH c.lineas l
        LEFT JOIN FETCH l.plato
        WHERE c.id = :id
        """)
    Optional<Comanda> findByIdWithLineas(@Param("id") Long id);

    /**
     * Comandas activas de una mesa para calcular el ticket.
     */
    @Query("""
        SELECT DISTINCT c FROM Comanda c
        LEFT JOIN FETCH c.lineas l
        LEFT JOIN FETCH l.plato
        WHERE c.mesa.id = :mesaId
        AND c.estado NOT IN ('ENTREGADA')
        """)
    List<Comanda> findComandasActivasPorMesa(@Param("mesaId") Long mesaId);
}
