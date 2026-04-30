package com.costasruben.ovellanegra.controller;


import com.costasruben.ovellanegra.entities.Inventario;
import com.costasruben.ovellanegra.service.InventarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}