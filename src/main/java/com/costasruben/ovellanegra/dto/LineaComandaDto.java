package com.costasruben.ovellanegra.dto;

import com.costasruben.ovellanegra.enums.EstadoLinea;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class LineaComandaDto {
    private Long id;
    private Long platoId;
    private String platoNombre;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private String observaciones;
    private EstadoLinea estado;   // ← nuevo
}
