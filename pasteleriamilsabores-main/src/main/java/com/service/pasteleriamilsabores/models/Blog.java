package com.service.pasteleriamilsabores.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "blogs")
public class Blog {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "titulo")
    private String titulo;
    @Column(name = "resumen")
    private String resumen;
    @Column(name = "imagen")
    private String imagen;
    @Column(name = "fecha")
    private String fecha;
    @Column(name = "autor")
    private String autor;
    @Column(name = "link")
    private String link;
}
