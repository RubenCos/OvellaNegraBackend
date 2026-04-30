package com.costasruben.ovellanegra.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PagarTicketRequest {
    @NotNull
    private Long mesaId;

    @NotBlank
    private String metodoPago; // EFECTIVO | TARJETA | BIZUM
}
