package com.costasruben.ovellanegra.service;

import com.costasruben.ovellanegra.dto.MesaDto;
import com.costasruben.ovellanegra.enums.EstadoMesa;

import java.util.List;

public interface MesaService {
    List<MesaDto> listarTodas();
    MesaDto obtenerPorId(Long id);
    MesaDto obtenerPorNumero(Integer numero);
    MesaDto crear(MesaDto dto);
    MesaDto cambiarEstado(Long id, EstadoMesa estado);
}
