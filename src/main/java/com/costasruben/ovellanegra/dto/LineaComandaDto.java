package com.costasruben.ovellanegra.dto;

import com.costasruben.ovellanegra.enums.EstadoLinea;
import com.costasruben.ovellanegra.enums.Responsable;
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
    private EstadoLinea estado;
    private Responsable responsable;   // COCINA o BARRA
}
