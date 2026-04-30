package com.costasruben.ovellanegra.repository;

import com.costasruben.ovellanegra.entities.Comanda;
import com.costasruben.ovellanegra.enums.EstadoComanda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ComandaRepository extends JpaRepository<Comanda, Long> {

    List<Comanda> findByMesaId(Long mesaId);

    List<Comanda> findByEstado(EstadoComanda estado);

    /** Devuelve todas las comandas pendientes y en preparación (vista cocina) */
    @Query("SELECT c FROM Comanda c WHERE c.estado IN ('PENDIENTE','EN_PREPARACION') ORDER BY c.fechaHora ASC")
    List<Comanda> findComandasActivas();

    /** Comandas activas de una mesa concreta (para calcular ticket) */
    @Query("SELECT c FROM Comanda c WHERE c.mesa.id = :mesaId AND c.estado NOT IN ('ENTREGADA')")
    List<Comanda> findComandasActivasPorMesa(Long mesaId);
}
