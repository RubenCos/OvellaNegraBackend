package com.costasruben.ovellanegra.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CrearComandaRequest {

    @NotNull
    private Long mesaId;

    private String notas;

    @NotNull
    private List<LineaRequest> lineas;

    @Data
    public static class LineaRequest {
        @NotNull
        private Long platoId;

        @NotNull
        @Min(1)
        private Integer cantidad;

        private String observaciones;
    }
}
