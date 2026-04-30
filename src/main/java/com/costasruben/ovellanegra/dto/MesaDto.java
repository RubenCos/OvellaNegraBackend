package com.costasruben.ovellanegra.dto;

import com.costasruben.ovellanegra.enums.EstadoMesa;
import lombok.Data;

@Data
public class MesaDto {
    private Long id;
    private Integer numero;
    private Integer capacidad;
    private EstadoMesa estado;
}