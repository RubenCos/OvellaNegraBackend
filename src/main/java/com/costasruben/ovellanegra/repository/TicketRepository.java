package com.costasruben.ovellanegra.repository;

import com.costasruben.ovellanegra.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByMesaId(Long mesaId);
    Optional<Ticket> findByMesaIdAndPagadoFalse(Long mesaId);
}
