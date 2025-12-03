package com.service.pasteleriamilsabores.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductosWrapper {
    @JsonProperty("nombre_pasteleria")
    private String nombrePasteleria;
    private List<Categoria> categorias;
}
