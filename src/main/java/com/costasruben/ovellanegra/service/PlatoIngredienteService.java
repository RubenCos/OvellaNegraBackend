package com.costasruben.ovellanegra.service;


import com.costasruben.ovellanegra.entities.PlatoIngrediente;

import java.util.List;

public interface PlatoIngredienteService {
    List<PlatoIngrediente> listarPorPlato(Long platoId);
    PlatoIngrediente asignar(Long platoId, Long ingredienteId, java.math.BigDecimal cantidadUsada);
    PlatoIngrediente actualizar(Long id, java.math.BigDecimal nuevaCantidad);
    void eliminar(Long id);
}
