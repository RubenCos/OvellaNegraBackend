package com.costasruben.ovellanegra.entities;

import com.costasruben.ovellanegra.enums.EstadoComanda;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comandas")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Comanda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mesa_id")
    private Mesa mesa;

    @Column(nullable = false)
    private LocalDateTime fechaHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoComanda estado;

    /** Notas adicionales del cliente (alergias, preferencias) */
    @Column(length = 300)
    private String notas;

    @OneToMany(mappedBy = "comanda", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<LineaComanda> lineas = new ArrayList<>();

    @PrePersist
    private void prePersist() {
        if (fechaHora == null) fechaHora = LocalDateTime.now();
        if (estado == null)    estado    = EstadoComanda.PENDIENTE;
    }
}