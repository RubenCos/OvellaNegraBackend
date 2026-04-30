package com.costasruben.ovellanegra.service;

import com.costasruben.ovellanegra.dto.ComandaDto;
import com.costasruben.ovellanegra.dto.CrearComandaRequest;
import com.costasruben.ovellanegra.enums.EstadoComanda;
import com.costasruben.ovellanegra.enums.EstadoLinea;

import java.util.List;

public interface ComandaService {
    ComandaDto crearComanda(CrearComandaRequest request);
    ComandaDto cambiarEstado(Long comandaId, EstadoComanda nuevoEstado);

    /**
     * Cambia el estado de una línea individual y recalcula el estado
     * global de su comanda padre automáticamente.
     *
     * Reglas de derivación:
     *  - Todas PENDIENTE          → comanda PENDIENTE
     *  - Al menos 1 EN_PREPARACION → comanda EN_PREPARACION
     *  - Todas LISTA              → comanda LISTA  (y descuenta stock)
     */
    ComandaDto cambiarEstadoLinea(Long comandaId, Long lineaId, EstadoLinea nuevoEstado);

    List<ComandaDto> obtenerComandasActivas();
    List<ComandaDto> obtenerPorMesa(Long mesaId);
    ComandaDto obtenerPorId(Long id);
}
