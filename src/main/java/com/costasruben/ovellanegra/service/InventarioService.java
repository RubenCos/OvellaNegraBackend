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
    void eliminar(Long id);
    /** Suma cantidad al stock actual (reposición) */
    Inventario reponer(Long id, BigDecimal cantidad);
    /** Descuenta stock al confirmar una comanda como LISTA */
    void descontarStockPorComanda(Long comandaId);
}
