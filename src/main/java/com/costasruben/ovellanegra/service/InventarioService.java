package com.costasruben.ovellanegra.service;

import com.costasruben.ovellanegra.entities.Inventario;
import java.math.BigDecimal;
import java.util.List;

public interface InventarioService {
    List<Inventario> listarTodo();
    List<Inventario> listarStockBajo();
    Inventario obtenerPorId(Long id);
    Inventario crear(Inventario inventario);
    Inventario actualizar(Long id, Inventario inventario);
    /** Descuenta stock al confirmar platos de una comanda */
    void descontarStockPorComanda(Long comandaId);
}
