package com.costasruben.ovellanegra.service;


import com.costasruben.ovellanegra.dto.PagarTicketRequest;
import com.costasruben.ovellanegra.dto.TicketDto;

public interface CobroService {
    /** Genera (o recupera) el ticket pendiente de una mesa */
    TicketDto generarTicket(Long mesaId);
    /** Registra el pago y libera la mesa */
    TicketDto pagar(PagarTicketRequest request);
    TicketDto obtenerTicketActivo(Long mesaId);
}
