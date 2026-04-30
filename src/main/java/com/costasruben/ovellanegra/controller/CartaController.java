package com.costasruben.ovellanegra.controller;


import com.costasruben.ovellanegra.dto.PlatoDto;
import com.costasruben.ovellanegra.enums.CategoriaPlato;
import com.costasruben.ovellanegra.service.PlatoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carta")
@RequiredArgsConstructor
public class CartaController {

    private final PlatoService platoService;

    /** GET /api/carta — todos los platos disponibles */
    @GetMapping
    public List<PlatoDto> getCarta() {
        return platoService.obtenerCarta();
    }

    /** GET /api/carta/categoria/{cat} */
    @GetMapping("/categoria/{categoria}")
    public List<PlatoDto> getPorCategoria(@PathVariable CategoriaPlato categoria) {
        return platoService.obtenerPorCategoria(categoria);
    }

    /** GET /api/carta/{id} */
    @GetMapping("/{id}")
    public PlatoDto getById(@PathVariable Long id) {
        return platoService.obtenerPorId(id);
    }

    /** POST /api/carta — crear plato (admin) */
    @PostMapping
    public ResponseEntity<PlatoDto> crear(@RequestBody PlatoDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(platoService.crear(dto));
    }

    /** PUT /api/carta/{id} */
    @PutMapping("/{id}")
    public PlatoDto actualizar(@PathVariable Long id, @RequestBody PlatoDto dto) {
        return platoService.actualizar(id, dto);
    }

    /** PATCH /api/carta/{id}/disponibilidad */
    @PatchMapping("/{id}/disponibilidad")
    public ResponseEntity<Void> disponibilidad(@PathVariable Long id, @RequestParam boolean disponible) {
        platoService.cambiarDisponibilidad(id, disponible);
        return ResponseEntity.noContent().build();
    }

    /** DELETE /api/carta/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        platoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}