package com.costasruben.ovellanegra.entities;

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
    private Comanda comanda;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plato_id")
    private Plato plato;

    @Column(nullable = false)
    private Integer cantidad;

    /** Precio unitario en el momento del pedido */
    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal precioUnitario;

    @Column(length = 200)
    private String observaciones;
}
