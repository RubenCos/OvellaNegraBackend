package com.costasruben.ovellanegra.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TicketDto {
    private Long id;
    private Long mesaId;
    private Integer mesaNumero;
    private LocalDateTime fechaEmision;
    private BigDecimal subtotal;
    private BigDecimal ivaPorc;
    private BigDecimal totalConIva;
    private String metodoPago;
    private Boolean pagado;
    private List<ComandaDto> comandas;
}
