package com.costasruben.ovellanegra.service.impl;

import com.costasruben.ovellanegra.dto.*;
import com.costasruben.ovellanegra.entities.*;
import com.costasruben.ovellanegra.enums.EstadoMesa;
import com.costasruben.ovellanegra.exception.RestauranteException;
import com.costasruben.ovellanegra.repository.*;
import com.costasruben.ovellanegra.service.CobroService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Service
@RequiredArgsConstructor
public class CobroServiceImpl implements CobroService {

    private final TicketRepository ticketRepository;
    private final MesaRepository mesaRepository;
    private final ComandaRepository comandaRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    @Transactional
    public TicketDto generarTicket(Long mesaId) {
        Mesa mesa = findMesa(mesaId);

        // Reutilizar ticket si ya existe uno pendiente
        return ticketRepository.findByMesaIdAndPagadoFalse(mesaId)
                .map(t -> toDto(t, mesa))
                .orElseGet(() -> {
                    BigDecimal subtotal = calcularSubtotal(mesaId);
                    BigDecimal ivaPorc = new BigDecimal("10.00");
                    BigDecimal totalConIva = subtotal
                            .multiply(BigDecimal.ONE.add(ivaPorc.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP)))
                            .setScale(2, RoundingMode.HALF_UP);

                    Ticket ticket = Ticket.builder()
                            .mesa(mesa)
                            .subtotal(subtotal)
                            .ivaPorc(ivaPorc)
                            .totalConIva(totalConIva)
                            .pagado(false)
                            .build();

                    mesa.setEstado(EstadoMesa.ESPERANDO_COBRO);
                    mesaRepository.save(mesa);

                    return toDto(ticketRepository.save(ticket), mesa);
                });
    }

    @Override
    @Transactional
    public TicketDto pagar(PagarTicketRequest request) {
        Ticket ticket = ticketRepository.findByMesaIdAndPagadoFalse(request.getMesaId())
                .orElseThrow(() -> new RestauranteException("No hay ticket pendiente para la mesa: " + request.getMesaId()));

        ticket.setMetodoPago(request.getMetodoPago());
        ticket.setPagado(true);
        Ticket saved = ticketRepository.save(ticket);

        // Liberar mesa
        Mesa mesa = ticket.getMesa();
        mesa.setEstado(EstadoMesa.LIBRE);
        mesaRepository.save(mesa);

        TicketDto dto = toDto(saved, mesa);

        // Notificar a la mesa que el pago está confirmado
        messagingTemplate.convertAndSend("/topic/mesa/" + mesa.getId(), dto);
        log.info("Mesa {} liberada tras pago de {}€ ({}) ", mesa.getNumero(), saved.getTotalConIva(), saved.getMetodoPago());

        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public TicketDto obtenerTicketActivo(Long mesaId) {
        Mesa mesa = findMesa(mesaId);
        Ticket ticket = ticketRepository.findByMesaIdAndPagadoFalse(mesaId)
                .orElseThrow(() -> new RestauranteException("No hay ticket pendiente para la mesa: " + mesaId));
        return toDto(ticket, mesa);
    }

    // ── helpers ───────────────────────────────────────────────

    private Mesa findMesa(Long mesaId) {
        return mesaRepository.findById(mesaId)
                .orElseThrow(() -> new EntityNotFoundException("Mesa no encontrada: " + mesaId));
    }

    private BigDecimal calcularSubtotal(Long mesaId) {
        return comandaRepository.findComandasActivasPorMesa(mesaId).stream()
                .flatMap(c -> c.getLineas().stream())
                .map(l -> l.getPrecioUnitario().multiply(BigDecimal.valueOf(l.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private TicketDto toDto(Ticket t, Mesa mesa) {
        TicketDto dto = new TicketDto();
        dto.setId(t.getId());
        dto.setMesaId(mesa.getId());
        dto.setMesaNumero(mesa.getNumero());
        dto.setFechaEmision(t.getFechaEmision());
        dto.setSubtotal(t.getSubtotal());
        dto.setIvaPorc(t.getIvaPorc());
        dto.setTotalConIva(t.getTotalConIva());
        dto.setMetodoPago(t.getMetodoPago());
        dto.setPagado(t.getPagado());
        return dto;
    }
}