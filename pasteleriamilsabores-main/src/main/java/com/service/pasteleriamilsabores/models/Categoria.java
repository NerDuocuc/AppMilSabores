package com.service.pasteleriamilsabores.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categorias")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id_categoria")
    @Column(name = "id_categoria")
    private Integer idCategoria;
    
    @JsonProperty("nombre_categoria")
    @Column(name = "nombre_categoria")
    private String nombreCategoria;
    
    @OneToMany(mappedBy = "categoria", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
    private final List<Producto> productos = new ArrayList<>();

    // keep helper methods to maintain bidirectional relation
    public void setProductos(List<Producto> productos) {
        this.productos.clear();
        if (productos != null) {
            for (Producto p : productos) {
                p.setCategoria(this);
                this.productos.add(p);
            }
        }
    }
    public void addProducto(Producto producto) {
        producto.setCategoria(this);
        this.productos.add(producto);
    }
    public void removeProducto(Producto producto) {
        producto.setCategoria(null);
        this.productos.remove(producto);
    }
}
