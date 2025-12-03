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
@Table(name = "direcciones")
public class Address {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "direccion")
    private String address;
    @Column(name = "region")
    private String region;
    @Column(name = "comuna")
    private String comuna;

    @ManyToOne
    @JoinColumn(name = "usuario_run")
    private User user;
}
