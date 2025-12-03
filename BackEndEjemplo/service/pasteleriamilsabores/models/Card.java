package com.service.pasteleriamilsabores.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tarjetas")
public class Card {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "numero_tarjeta", nullable = false)
    private String cardNumber;

    @Column(name = "mes")
    private Integer month;

    @Column(name = "anio")
    private Integer year;

    @Column(name = "nombre")
    private String cardholderName;

    @ManyToOne
    @JoinColumn(name = "usuario_run")
    private User user;
}
