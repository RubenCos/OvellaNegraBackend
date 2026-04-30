package com.costasruben.ovellanegra.entities;


import com.costasruben.ovellanegra.enums.CategoriaPlato;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "platos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Plato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal precio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaPlato categoria;

    @Column(nullable = false)
    private Boolean disponible = true;

    /** URL relativa a la imagen del plato */
    private String imagenUrl;
}