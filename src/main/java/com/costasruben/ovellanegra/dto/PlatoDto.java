package com.costasruben.ovellanegra.dto;

import com.costasruben.ovellanegra.enums.CategoriaPlato;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PlatoDto {
    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private CategoriaPlato categoria;
    private Boolean disponible;
    private String imagenUrl;
}
