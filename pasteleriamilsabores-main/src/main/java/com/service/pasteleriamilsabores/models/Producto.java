package com.service.pasteleriamilsabores.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "productos")
public class Producto {
    @Id
    @JsonProperty("codigo_producto")
    @Column(name = "codigo_producto")
    private String codigoProducto;
    @JsonProperty("nombre_producto")
    @Column(name = "nombre_producto")
    private String nombreProducto;
    @JsonProperty("precio_producto")
    @Column(name = "precio_producto")
    private Integer precioProducto;
    @JsonProperty("descripción_producto")
    @Lob
    @Column(name = "descripcion_producto", columnDefinition = "TEXT")
    private String descripcionProducto;
    @JsonProperty("imagen_producto")
    @Column(name = "imagen_producto")
    private String imagenProducto;
    @Column(name = "stock")
    private Integer stock;
    @JsonProperty("stock_critico")
    @Column(name = "stock_critico")
    private Integer stockCritico;
    @Transient
    private List<Object> ratings;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
}
