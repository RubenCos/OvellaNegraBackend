package com.costasruben.ovellanegra.service.impl;


import com.costasruben.ovellanegra.dto.MesaDto;
import com.costasruben.ovellanegra.entities.Mesa;
import com.costasruben.ovellanegra.enums.EstadoMesa;
import com.costasruben.ovellanegra.repository.MesaRepository;
import com.costasruben.ovellanegra.service.MesaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MesaServiceImpl implements MesaService {

    private final MesaRepository mesaRepository;

    @Override
    public List<MesaDto> listarTodas() {
        return mesaRepository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public MesaDto obtenerPorId(Long id) {
        return toDto(findById(id));
    }

    @Override
    public MesaDto obtenerPorNumero(Integer numero) {
        return toDto(mesaRepository.findByNumero(numero)
                .orElseThrow(() -> new EntityNotFoundException("Mesa no encontrada: " + numero)));
    }

    @Override
    @Transactional
    public MesaDto crear(MesaDto dto) {
        Mesa mesa = Mesa.builder()
                .numero(dto.getNumero())
                .capacidad(dto.getCapacidad())
                .estado(EstadoMesa.LIBRE)
                .build();
        return toDto(mesaRepository.save(mesa));
    }

    @Override
    @Transactional
    public MesaDto cambiarEstado(Long id, EstadoMesa estado) {
        Mesa mesa = findById(id);
        mesa.setEstado(estado);
        return toDto(mesaRepository.save(mesa));
    }

    // ── helpers ───────────────────────────────────────────────

    private Mesa findById(Long id) {
        return mesaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mesa no encontrada: " + id));
    }

    private MesaDto toDto(Mesa m) {
        MesaDto dto = new MesaDto();
        dto.setId(m.getId());
        dto.setNumero(m.getNumero());
        dto.setCapacidad(m.getCapacidad());
        dto.setEstado(m.getEstado());
        return dto;
    }
}