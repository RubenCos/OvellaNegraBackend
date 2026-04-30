package com.costasruben.ovellanegra.controller;


import com.costasruben.ovellanegra.dto.MesaDto;
import com.costasruben.ovellanegra.enums.EstadoMesa;
import com.costasruben.ovellanegra.service.MesaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mesas")
@RequiredArgsConstructor
public class MesaController {

    private final MesaService mesaService;

    @GetMapping
    public List<MesaDto> listar() {
        return mesaService.listarTodas();
    }

    @GetMapping("/{id}")
    public MesaDto getById(@PathVariable Long id) {
        return mesaService.obtenerPorId(id);
    }

    @GetMapping("/numero/{numero}")
    public MesaDto getByNumero(@PathVariable Integer numero) {
        return mesaService.obtenerPorNumero(numero);
    }

    @PostMapping
    public ResponseEntity<MesaDto> crear(@RequestBody MesaDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mesaService.crear(dto));
    }

    @PatchMapping("/{id}/estado")
    public MesaDto cambiarEstado(@PathVariable Long id, @RequestParam EstadoMesa estado) {
        return mesaService.cambiarEstado(id, estado);
    }
}
