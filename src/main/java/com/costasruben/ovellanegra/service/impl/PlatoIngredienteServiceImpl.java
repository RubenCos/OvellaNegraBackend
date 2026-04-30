package com.costasruben.ovellanegra.service.impl;


import com.costasruben.ovellanegra.entities.Inventario;
import com.costasruben.ovellanegra.entities.Plato;
import com.costasruben.ovellanegra.entities.PlatoIngrediente;
import com.costasruben.ovellanegra.repository.InventarioRepository;
import com.costasruben.ovellanegra.repository.PlatoIngredienteRepository;
import com.costasruben.ovellanegra.repository.PlatoRepository;
import com.costasruben.ovellanegra.service.PlatoIngredienteService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlatoIngredienteServiceImpl implements PlatoIngredienteService {

    private final PlatoIngredienteRepository platoIngredienteRepository;
    private final PlatoRepository platoRepository;
    private final InventarioRepository inventarioRepository;

    @Override
    public List<PlatoIngrediente> listarPorPlato(Long platoId) {
        return platoIngredienteRepository.findByPlatoId(platoId);
    }

    @Override
    @Transactional
    public PlatoIngrediente asignar(Long platoId, Long ingredienteId, BigDecimal cantidadUsada) {
        Plato plato = platoRepository.findById(platoId)
                .orElseThrow(() -> new EntityNotFoundException("Plato no encontrado: " + platoId));

        Inventario ingrediente = inventarioRepository.findById(ingredienteId)
                .orElseThrow(() -> new EntityNotFoundException("Ingrediente no encontrado: " + ingredienteId));

        // Evitar duplicados: si ya existe esa relación, actualizamos la cantidad
        return platoIngredienteRepository.findByPlatoId(platoId).stream()
                .filter(pi -> pi.getIngrediente().getId().equals(ingredienteId))
                .findFirst()
                .map(pi -> { pi.setCantidadUsada(cantidadUsada); return platoIngredienteRepository.save(pi); })
                .orElseGet(() -> platoIngredienteRepository.save(
                        PlatoIngrediente.builder()
                                .plato(plato)
                                .ingrediente(ingrediente)
                                .cantidadUsada(cantidadUsada)
                                .build()
                ));
    }

    @Override
    @Transactional
    public PlatoIngrediente actualizar(Long id, BigDecimal nuevaCantidad) {
        PlatoIngrediente pi = platoIngredienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Relación no encontrada: " + id));
        pi.setCantidadUsada(nuevaCantidad);
        return platoIngredienteRepository.save(pi);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!platoIngredienteRepository.existsById(id)) {
            throw new EntityNotFoundException("Relación no encontrada: " + id);
        }
        platoIngredienteRepository.deleteById(id);
    }
}