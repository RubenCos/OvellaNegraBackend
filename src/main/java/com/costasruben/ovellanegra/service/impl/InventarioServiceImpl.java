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

    private final InventarioRepository       inventarioRepository;
    private final ComandaRepository          comandaRepository;
    private final PlatoIngredienteRepository platoIngredienteRepository;

    @Override
    public List<Inventario> listarTodo() { return inventarioRepository.findAll(); }

    @Override
    public List<Inventario> listarStockBajo() { return inventarioRepository.findStockBajo(); }

    @Override
    public Inventario obtenerPorId(Long id) {
        return inventarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ingrediente no encontrado: " + id));
    }

    @Override @Transactional
    public Inventario crear(Inventario inventario) { return inventarioRepository.save(inventario); }

    @Override @Transactional
    public Inventario actualizar(Long id, Inventario datos) {
        Inventario inv = obtenerPorId(id);
        inv.setNombreIngrediente(datos.getNombreIngrediente());
        inv.setCantidad(datos.getCantidad());
        inv.setUnidad(datos.getUnidad());
        inv.setStockMinimo(datos.getStockMinimo());
        return inventarioRepository.save(inv);
    }

    @Override @Transactional
    public void eliminar(Long id) {
        if (!inventarioRepository.existsById(id))
            throw new EntityNotFoundException("Ingrediente no encontrado: " + id);
        inventarioRepository.deleteById(id);
    }

    @Override @Transactional
    public Inventario reponer(Long id, BigDecimal cantidad) {
        Inventario inv = obtenerPorId(id);
        inv.setCantidad(inv.getCantidad().add(cantidad));
        log.info("Reposición '{}': +{} {} → total {}",
                inv.getNombreIngrediente(), cantidad, inv.getUnidad(), inv.getCantidad());
        return inventarioRepository.save(inv);
    }

    /**
     * Descuenta del inventario los ingredientes consumidos por cada línea
     * de la comanda. Se llama cuando el cocinero marca la comanda como LISTA.
     */
    @Override @Transactional
    public void descontarStockPorComanda(Long comandaId) {

        // JOIN FETCH: carga lineas + plato en una sola query, sin LAZY
        Comanda comanda = comandaRepository.findByIdWithLineas(comandaId)
                .orElseThrow(() -> new EntityNotFoundException("Comanda no encontrada: " + comandaId));

        if (comanda.getLineas() == null || comanda.getLineas().isEmpty()) {
            log.warn("Comanda #{} no tiene líneas, nada que descontar.", comandaId);
            return;
        }

        for (LineaComanda linea : comanda.getLineas()) {
            Long platoId = linea.getPlato().getId();

            // JOIN FETCH: carga el Inventario dentro de cada PlatoIngrediente
            List<PlatoIngrediente> ingredientes =
                    platoIngredienteRepository.findByPlatoIdWithIngrediente(platoId);

            if (ingredientes.isEmpty()) {
                log.debug("Plato '{}' sin ingredientes asignados, se omite.",
                        linea.getPlato().getNombre());
                continue;
            }

            for (PlatoIngrediente pi : ingredientes) {
                Inventario inv   = pi.getIngrediente();
                BigDecimal consumo = pi.getCantidadUsada()
                        .multiply(BigDecimal.valueOf(linea.getCantidad()));

                BigDecimal antes  = inv.getCantidad();
                BigDecimal nuevo  = antes.subtract(consumo).max(BigDecimal.ZERO);
                inv.setCantidad(nuevo);
                inventarioRepository.save(inv);

                log.info("Stock '{}': {} - {} = {} {}",
                        inv.getNombreIngrediente(), antes, consumo, nuevo, inv.getUnidad());

                if (nuevo.compareTo(inv.getStockMinimo()) < 0)
                    log.warn("⚠ STOCK BAJO: '{}' → {} {} (mín: {})",
                            inv.getNombreIngrediente(), nuevo, inv.getUnidad(), inv.getStockMinimo());
            }
        }
    }
}
