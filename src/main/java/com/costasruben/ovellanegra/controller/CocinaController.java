package com.costasruben.ovellanegra.controller;


import com.costasruben.ovellanegra.dto.ComandaDto;
import com.costasruben.ovellanegra.dto.CrearComandaRequest;
import com.costasruben.ovellanegra.enums.EstadoComanda;
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

    /** POST /api/comandas — la tablet de la mesa crea una comanda */
    @PostMapping
    public ResponseEntity<ComandaDto> crear(@Valid @RequestBody CrearComandaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(comandaService.crearComanda(request));
    }

    /** GET /api/comandas/activas — vista de cocina (polling o inicial) */
    @GetMapping("/activas")
    public List<ComandaDto> getActivas() {
        return comandaService.obtenerComandasActivas();
    }

    /** GET /api/comandas/mesa/{mesaId} */
    @GetMapping("/mesa/{mesaId}")
    public List<ComandaDto> getPorMesa(@PathVariable Long mesaId) {
        return comandaService.obtenerPorMesa(mesaId);
    }

    /** GET /api/comandas/{id} */
    @GetMapping("/{id}")
    public ComandaDto getById(@PathVariable Long id) {
        return comandaService.obtenerPorId(id);
    }

    /** PATCH /api/comandas/{id}/estado — cocinero confirma estado */
    @PatchMapping("/{id}/estado")
    public ComandaDto cambiarEstado(@PathVariable Long id, @RequestParam EstadoComanda estado) {
        return comandaService.cambiarEstado(id, estado);
    }

    // ── WebSocket (STOMP) ─────────────────────────────────────

    /**
     * El cocinero envía al canal /app/cocina/confirmar un mensaje con el
     * id de comanda y el nuevo estado. Se rebroadcast a /topic/cocina.
     */
    @MessageMapping("/cocina/confirmar")
    @SendTo("/topic/cocina")
    public ComandaDto confirmarViaWs(ConfirmarEstadoMessage msg) {
        return comandaService.cambiarEstado(msg.comandaId(), msg.estado());
    }

    /** Record usado como payload del mensaje WebSocket */
    public record ConfirmarEstadoMessage(Long comandaId, EstadoComanda estado) {}
}