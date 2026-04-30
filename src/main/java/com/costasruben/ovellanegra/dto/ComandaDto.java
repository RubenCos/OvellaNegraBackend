package com.costasruben.ovellanegra.dto;

import com.costasruben.ovellanegra.enums.EstadoComanda;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ComandaDto {
    private Long id;
    private Long mesaId;
    private Integer mesaNumero;
    private LocalDateTime fechaHora;
    private EstadoComanda estado;
    private String notas;
    private List<LineaComandaDto> lineas;
}
