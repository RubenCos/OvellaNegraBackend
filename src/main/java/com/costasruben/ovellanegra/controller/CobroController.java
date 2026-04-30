package com.costasruben.ovellanegra.controller;

import com.costasruben.ovellanegra.dto.PagarTicketRequest;
import com.costasruben.ovellanegra.dto.TicketDto;
import com.costasruben.ovellanegra.service.CobroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cobro")
@RequiredArgsConstructor
public class CobroController {

    private final CobroService cobroService;

    /** POST /api/cobro/ticket/{mesaId} — genera ticket para una mesa */
    @PostMapping("/ticket/{mesaId}")
    public TicketDto generarTicket(@PathVariable Long mesaId) {
        return cobroService.generarTicket(mesaId);
    }

    /** GET /api/cobro/ticket/{mesaId} — consulta ticket activo */
    @GetMapping("/ticket/{mesaId}")
    public TicketDto getTicketActivo(@PathVariable Long mesaId) {
        return cobroService.obtenerTicketActivo(mesaId);
    }

    /** POST /api/cobro/pagar — registra el pago */
    @PostMapping("/pagar")
    public TicketDto pagar(@Valid @RequestBody PagarTicketRequest request) {
        return cobroService.pagar(request);
    }
}
