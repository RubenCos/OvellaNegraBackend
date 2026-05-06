package com.costasruben.ovellanegra.entities;


import com.costasruben.ovellanegra.enums.CategoriaPlato;
import com.costasruben.ovellanegra.enums.Responsable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
    @Builder.Default
    private Boolean disponible = true;

    private String imagenUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Responsable responsable = Responsable.COCINA;

    @JsonIgnore
    @OneToMany(mappedBy = "plato", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PlatoIngrediente> ingredientes = new ArrayList<>();

    /**
     * Solo en INSERT: si no se asignó responsable explícitamente,
     * lo deriva de la categoría. En UPDATE nunca se toca el campo.
     */
    @PrePersist
    private void prePersist() {
        if (responsable == null) {
            responsable = (categoria == CategoriaPlato.BEBIDA)
                    ? Responsable.BARRA
                    : Responsable.COCINA;
        }
    }
}
