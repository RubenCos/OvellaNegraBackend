package com.costasruben.ovellanegra.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Relaciona un plato con los ingredientes del inventario que consume.
 * Al confirmar una comanda se descuenta {@code cantidadUsada} por ración.
 */
@Entity
@Table(name = "plato_ingredientes")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PlatoIngrediente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plato_id")
    private Plato plato;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "inventario_id")
    private Inventario ingrediente;

    /** Cantidad del ingrediente que consume una ración de este plato */
    @Column(nullable = false, precision = 10, scale = 3)
    private BigDecimal cantidadUsada;
}
