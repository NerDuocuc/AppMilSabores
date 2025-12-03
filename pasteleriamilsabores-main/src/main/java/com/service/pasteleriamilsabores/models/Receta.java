package com.service.pasteleriamilsabores.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "recetas")
public class Receta {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "titulo")
    private String titulo;
    @Lob
    @Column(name = "resumen", columnDefinition = "TEXT")
    private String resumen;

    @Lob
    @Column(name = "ingredientes", columnDefinition = "TEXT")
    private String ingredientes;

    @Lob
    @Column(name = "preparacion", columnDefinition = "TEXT")
    private String preparacion;
    @Column(name = "imagen")
    private String imagen;
    @Column(name = "badge")
    private String badge;
    @Column(name = "badge_class")
    private String badgeClass;
}
