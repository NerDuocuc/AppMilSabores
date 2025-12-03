package com.service.pasteleriamilsabores.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CategoriaDto {
    @JsonProperty("id_categoria")
    private Integer id;
    @JsonProperty("nombre_categoria")
    private String nombre;
    @JsonProperty("categoria_slug")
    private String slug;

    public CategoriaDto() {}

    public CategoriaDto(Integer id, String nombre, String slug) {
        this.id = id;
        this.nombre = nombre;
        this.slug = slug;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
}