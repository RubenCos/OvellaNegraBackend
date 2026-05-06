package com.costasruben.ovellanegra.service.impl;

import com.costasruben.ovellanegra.dto.PlatoDto;
import com.costasruben.ovellanegra.entities.Plato;
import com.costasruben.ovellanegra.enums.CategoriaPlato;
import com.costasruben.ovellanegra.enums.Responsable;
import com.costasruben.ovellanegra.repository.PlatoRepository;
import com.costasruben.ovellanegra.service.PlatoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlatoServiceImpl implements PlatoService {

    private final PlatoRepository platoRepository;

    @Override
    public List<PlatoDto> obtenerCarta() {
        return platoRepository.findByDisponibleTrue().stream().map(this::toDto).toList();
    }

    @Override
    public List<PlatoDto> obtenerPorCategoria(CategoriaPlato categoria) {
        return platoRepository.findByCategoriaAndDisponibleTrue(categoria).stream().map(this::toDto).toList();
    }

    @Override
    public PlatoDto obtenerPorId(Long id) {
        return toDto(findById(id));
    }

    @Override
    @Transactional
    public PlatoDto crear(PlatoDto dto) {
        Plato plato = Plato.builder()
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .precio(dto.getPrecio())
                .categoria(dto.getCategoria())
                .disponible(dto.getDisponible() != null ? dto.getDisponible() : true)
                .imagenUrl(dto.getImagenUrl())
                // Si el frontend envía responsable explícito, se usa; si no, @PrePersist lo deriva
                .responsable(dto.getResponsable() != null
                        ? dto.getResponsable()
                        : (dto.getCategoria() == CategoriaPlato.BEBIDA ? Responsable.BARRA : Responsable.COCINA))
                .build();
        return toDto(platoRepository.save(plato));
    }

    @Override
    @Transactional
    public PlatoDto actualizar(Long id, PlatoDto dto) {
        Plato plato = findById(id);
        plato.setNombre(dto.getNombre());
        plato.setDescripcion(dto.getDescripcion());
        plato.setPrecio(dto.getPrecio());
        plato.setCategoria(dto.getCategoria());
        plato.setImagenUrl(dto.getImagenUrl());
        if (dto.getResponsable() != null) {
            plato.setResponsable(dto.getResponsable());
        }
        return toDto(platoRepository.save(plato));
    }

    @Override
    @Transactional
    public void cambiarDisponibilidad(Long id, boolean disponible) {
        Plato plato = findById(id);
        plato.setDisponible(disponible);
        platoRepository.save(plato);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        platoRepository.delete(findById(id));
    }

    // ── helpers ───────────────────────────────────────────────

    private Plato findById(Long id) {
        return platoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Plato no encontrado: " + id));
    }

    public PlatoDto toDto(Plato p) {
        PlatoDto dto = new PlatoDto();
        dto.setId(p.getId());
        dto.setNombre(p.getNombre());
        dto.setDescripcion(p.getDescripcion());
        dto.setPrecio(p.getPrecio());
        dto.setCategoria(p.getCategoria());
        dto.setDisponible(p.getDisponible());
        dto.setImagenUrl(p.getImagenUrl());
        dto.setResponsable(p.getResponsable());
        return dto;
    }
}