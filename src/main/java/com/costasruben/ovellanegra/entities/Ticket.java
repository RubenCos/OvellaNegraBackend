package com.costasruben.ovellanegra.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mesa_id")
    private Mesa mesa;

    @Column(nullable = false)
    private LocalDateTime fechaEmision = LocalDateTime.now();

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal ivaPorc = new BigDecimal("10.00");

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalConIva;

    /** Méodo de pago: EFECTIVO, TARJETA, BIZUM */
    @Column(length = 20)
    private String metodoPago;

    private Boolean pagado = false;
}