package com.costasruben.ovellanegra.entities;

import com.costasruben.ovellanegra.enums.EstadoLinea;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "lineas_comanda")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LineaComanda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "comanda_id")
    @JsonIgnoreProperties({"lineas", "hibernateLazyInitializer", "handler"})
    private Comanda comanda;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plato_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Plato plato;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal precioUnitario;

    @Column(length = 200)
    private String observaciones;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private EstadoLinea estado = EstadoLinea.PENDIENTE;
}
