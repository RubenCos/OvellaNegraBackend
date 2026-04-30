package com.costasruben.ovellanegra.controller;


import com.costasruben.ovellanegra.dto.ComandaDto;
import com.costasruben.ovellanegra.dto.CrearComandaRequest;
import com.costasruben.ovellanegra.enums.EstadoComanda;
import com.costasruben.ovellanegra.enums.EstadoLinea;
import com.costasruben.ovellanegra.service.ComandaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comandas")
@RequiredArgsConstructor
public class CocinaController {

    private final ComandaService comandaService;

    // ── REST ──────────────────────────────────────────────────

    @PostMapping
    public ResponseEntity<ComandaDto> crear(@Valid @RequestBody CrearComandaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(comandaService.crearComanda(request));
    }

    @GetMapping("/activas")
    public List<ComandaDto> getActivas() {
        return comandaService.obtenerComandasActivas();
    }

    @GetMapping("/mesa/{mesaId}")
    public List<ComandaDto> getPorMesa(@PathVariable Long mesaId) {
        return comandaService.obtenerPorMesa(mesaId);
    }

    @GetMapping("/{id}")
    public ComandaDto getById(@PathVariable Long id) {
        return comandaService.obtenerPorId(id);
    }

    /**
     * Cambia el estado de la comanda completa (usado para ENTREGADA).
     * PATCH /api/comandas/{id}/estado?estado=ENTREGADA
     */
    @PatchMapping("/{id}/estado")
    public ComandaDto cambiarEstado(@PathVariable Long id, @RequestParam EstadoComanda estado) {
        return comandaService.cambiarEstado(id, estado);
    }

    /**
     * Cambia el estado de UNA LÍNEA individual dentro de la comanda.
     * El estado global de la comanda se recalcula automáticamente.
     * PATCH /api/comandas/{comandaId}/lineas/{lineaId}/estado?estado=EN_PREPARACION
     */
    @PatchMapping("/{comandaId}/lineas/{lineaId}/estado")
    public ComandaDto cambiarEstadoLinea(
            @PathVariable Long comandaId,
            @PathVariable Long lineaId,
            @RequestParam EstadoLinea estado) {
        return comandaService.cambiarEstadoLinea(comandaId, lineaId, estado);
    }

    // ── WebSocket (STOMP) ─────────────────────────────────────

    @MessageMapping("/cocina/confirmar")
    @SendTo("/topic/cocina")
    public ComandaDto confirmarViaWs(ConfirmarEstadoMessage msg) {
        return comandaService.cambiarEstado(msg.comandaId(), msg.estado());
    }

    public record ConfirmarEstadoMessage(Long comandaId, EstadoComanda estado) {}
}