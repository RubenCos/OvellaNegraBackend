package com.costasruben.ovellanegra.service;

import com.costasruben.ovellanegra.dto.ComandaDto;
import com.costasruben.ovellanegra.dto.CrearComandaRequest;
import com.costasruben.ovellanegra.enums.EstadoComanda;

import java.util.List;

public interface ComandaService {
    ComandaDto crearComanda(CrearComandaRequest request);
    ComandaDto cambiarEstado(Long comandaId, EstadoComanda nuevoEstado);
    List<ComandaDto> obtenerComandasActivas();
    List<ComandaDto> obtenerPorMesa(Long mesaId);
    ComandaDto obtenerPorId(Long id);
}