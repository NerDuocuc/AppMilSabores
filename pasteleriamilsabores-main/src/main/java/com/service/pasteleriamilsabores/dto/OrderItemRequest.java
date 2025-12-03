package com.service.pasteleriamilsabores.dto;

public class OrderItemRequest {
    private String productoCodigo;
    private Integer cantidad;

    public String getProductoCodigo() { return productoCodigo; }
    public void setProductoCodigo(String productoCodigo) { this.productoCodigo = productoCodigo; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
}
