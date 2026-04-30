package com.costasruben.ovellanegra.service.impl;


import com.costasruben.ovellanegra.service.InventarioService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.costasruben.ovellanegra.repository.*;
import com.costasruben.ovellanegra.entities.*;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventarioServiceImpl implements InventarioService {

    private final InventarioRepository inventarioRepository;
    private final ComandaRepository comandaRepository;
    private final PlatoIngredienteRepository platoIngredienteRepository;

    @Override
    public List<Inventario> listarTodo() {
        return inventarioRepository.findAll();
    }

    @Override
    public List<Inventario> listarStockBajo() {
        return inventarioRepository.findStockBajo();
    }

    @Override
    public Inventario obtenerPorId(Long id) {
        return inventarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ingrediente no encontrado: " + id));
    }

    @Override
    @Transactional
    public Inventario crear(Inventario inventario) {
        return inventarioRepository.save(inventario);
    }

    @Override
    @Transactional
    public Inventario actualizar(Long id, Inventario datos) {
        Inventario inv = obtenerPorId(id);
        inv.setNombreIngrediente(datos.getNombreIngrediente());
        inv.setCantidad(datos.getCantidad());
        inv.setUnidad(datos.getUnidad());
        inv.setStockMinimo(datos.getStockMinimo());
        return inventarioRepository.save(inv);
    }

    @Override
    @Transactional
    public void descontarStockPorComanda(Long comandaId) {
        Comanda comanda = comandaRepository.findById(comandaId)
                .orElseThrow(() -> new EntityNotFoundException("Comanda no encontrada: " + comandaId));

        for (LineaComanda linea : comanda.getLineas()) {
            List<PlatoIngrediente> ingredientes = platoIngredienteRepository.findByPlatoId(linea.getPlato().getId());
            for (PlatoIngrediente pi : ingredientes) {
                Inventario inv = pi.getIngrediente();
                BigDecimal consumo = pi.getCantidadUsada().multiply(BigDecimal.valueOf(linea.getCantidad()));

                if (inv.getCantidad().compareTo(consumo) < 0) {
                    log.warn("Stock insuficiente de '{}': disponible={}, requerido={}",
                            inv.getNombreIngrediente(), inv.getCantidad(), consumo);
                    // En producción se puede lanzar excepción o crear alerta
                }

                inv.setCantidad(inv.getCantidad().subtract(consumo).max(BigDecimal.ZERO));
                inventarioRepository.save(inv);

                if (inv.getCantidad().compareTo(inv.getStockMinimo()) < 0) {
                    log.warn("ALERTA STOCK BAJO: '{}' ({} {})",
                            inv.getNombreIngrediente(), inv.getCantidad(), inv.getUnidad());
                }
            }
        }
    }
}