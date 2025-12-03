package com.service.pasteleriamilsabores.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductoDto {
    @JsonProperty("codigo_producto")
    private String codigoProducto;
    @JsonProperty("nombre_producto")
    private String nombreProducto;
    @JsonProperty("precio_producto")
    private Integer precioProducto;
    @JsonProperty("descripción_producto")
    private String descripcionProducto;
    @JsonProperty("imagen_producto")
    private String imagenProducto;
    private Integer stock;
    @JsonProperty("stock_critico")
    private Integer stockCritico;
    @JsonProperty("categoria_id")
    private Integer categoriaId;
    @JsonProperty("nombre_categoria")
    private String categoriaNombre;

    public ProductoDto() {}

    public String getCodigoProducto() { return codigoProducto; }
    public void setCodigoProducto(String codigoProducto) { this.codigoProducto = codigoProducto; }
    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
    public Integer getPrecioProducto() { return precioProducto; }
    public void setPrecioProducto(Integer precioProducto) { this.precioProducto = precioProducto; }
    public String getDescripcionProducto() { return descripcionProducto; }
    public void setDescripcionProducto(String descripcionProducto) { this.descripcionProducto = descripcionProducto; }
    public String getImagenProducto() { return imagenProducto; }
    public void setImagenProducto(String imagenProducto) { this.imagenProducto = imagenProducto; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public Integer getStockCritico() { return stockCritico; }
    public void setStockCritico(Integer stockCritico) { this.stockCritico = stockCritico; }
    public Integer getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Integer categoriaId) { this.categoriaId = categoriaId; }
    public String getCategoriaNombre() { return categoriaNombre; }
    public void setCategoriaNombre(String categoriaNombre) { this.categoriaNombre = categoriaNombre; }
}
