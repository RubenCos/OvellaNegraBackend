package com.costasruben.ovellanegra.entities;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "inventario")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombreIngrediente;

    /** Cantidad disponible en stock */
    @Column(nullable = false, precision = 10, scale = 3)
    private BigDecimal cantidad;

    /** Unidad de medida: kg, l, unidad, etc. */
    @Column(nullable = false, length = 20)
    private String unidad;

    /** Nivel mínimo para alerta de stock bajo */
    @Column(nullable = false, precision = 10, scale = 3)
    private BigDecimal stockMinimo;
}
