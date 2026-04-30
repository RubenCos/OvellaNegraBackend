package com.costasruben.ovellanegra.controller;


import com.costasruben.ovellanegra.entities.PlatoIngrediente;
import com.costasruben.ovellanegra.service.PlatoIngredienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/plato-ingredientes")
@RequiredArgsConstructor
public class PlatoIngredienteController {

    private final PlatoIngredienteService service;

    /** GET /api/plato-ingredientes/plato/{platoId} */
    @GetMapping("/plato/{platoId}")
    public List<PlatoIngrediente> listarPorPlato(@PathVariable Long platoId) {
        return service.listarPorPlato(platoId);
    }

    /**
     * POST /api/plato-ingredientes
     * Body: { "platoId": 1, "ingredienteId": 3, "cantidadUsada": 0.200 }
     */
    @PostMapping
    public ResponseEntity<PlatoIngrediente> asignar(@RequestBody Map<String, Object> body) {
        Long platoId      = Long.valueOf(body.get("platoId").toString());
        Long ingredienteId = Long.valueOf(body.get("ingredienteId").toString());
        BigDecimal cantidad = new BigDecimal(body.get("cantidadUsada").toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.asignar(platoId, ingredienteId, cantidad));
    }

    /**
     * PATCH /api/plato-ingredientes/{id}
     * Body: { "cantidadUsada": 0.150 }
     */
    @PatchMapping("/{id}")
    public PlatoIngrediente actualizar(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        BigDecimal cantidad = new BigDecimal(body.get("cantidadUsada").toString());
        return service.actualizar(id, cantidad);
    }

    /** DELETE /api/plato-ingredientes/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}