package com.costasruben.ovellanegra.controller;


import com.costasruben.ovellanegra.entities.Inventario;
import com.costasruben.ovellanegra.service.InventarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/inventario")
@RequiredArgsConstructor
public class InventarioController {

    private final InventarioService inventarioService;

    @GetMapping
    public List<Inventario> listar() {
        return inventarioService.listarTodo();
    }

    @GetMapping("/stock-bajo")
    public List<Inventario> stockBajo() {
        return inventarioService.listarStockBajo();
    }

    @GetMapping("/{id}")
    public Inventario getById(@PathVariable Long id) {
        return inventarioService.obtenerPorId(id);
    }

    @PostMapping
    public ResponseEntity<Inventario> crear(@RequestBody Inventario inventario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventarioService.crear(inventario));
    }

    @PutMapping("/{id}")
    public Inventario actualizar(@PathVariable Long id, @RequestBody Inventario inventario) {
        return inventarioService.actualizar(id, inventario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        inventarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * PATCH /api/inventario/{id}/reponer
     * Body: { "cantidad": 5.000 }
     * Suma la cantidad indicada al stock actual (reposición de mercancía).
     */
    @PatchMapping("/{id}/reponer")
    public Inventario reponer(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        BigDecimal cantidad = new BigDecimal(body.get("cantidad").toString());
        return inventarioService.reponer(id, cantidad);
    }
}