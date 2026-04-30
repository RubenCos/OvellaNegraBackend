package com.costasruben.ovellanegra.service;

import com.costasruben.ovellanegra.dto.PlatoDto;
import com.costasruben.ovellanegra.enums.CategoriaPlato;

import java.util.List;

public interface PlatoService {
    List<PlatoDto> obtenerCarta();
    List<PlatoDto> obtenerPorCategoria(CategoriaPlato categoria);
    PlatoDto obtenerPorId(Long id);
    PlatoDto crear(PlatoDto dto);
    PlatoDto actualizar(Long id, PlatoDto dto);
    void cambiarDisponibilidad(Long id, boolean disponible);
    void eliminar(Long id);
}
