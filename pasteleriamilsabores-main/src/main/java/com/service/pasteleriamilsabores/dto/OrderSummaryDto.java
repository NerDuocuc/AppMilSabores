package com.service.pasteleriamilsabores.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderSummaryDto {
    private String pedidoId;
    private String status;
    private LocalDateTime createdAt;
    private BigDecimal total;
    private String deliveryAddress;
    private Boolean freeCakeApplied;
    private BigDecimal freeCakeAmount;
    private Integer discountAppliedPercent;
    private Integer lifetimeDiscountAppliedPercent;
    private BigDecimal subtotal;
    private BigDecimal discountAmount;
    private BigDecimal totalSinDescuento;
    private BigDecimal totalConDescuento;
    private String purchaserRun;
    private String purchaserNombre;
    private String purchaserApellidos;
    private String purchaserCorreo;
    private String purchaserTelefono;
    private List<OrderItemSummaryDto> items;

    public String getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(String pedidoId) {
        this.pedidoId = pedidoId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public Boolean getFreeCakeApplied() {
        return freeCakeApplied;
    }

    public void setFreeCakeApplied(Boolean freeCakeApplied) {
        this.freeCakeApplied = freeCakeApplied;
    }

    public BigDecimal getFreeCakeAmount() {
        return freeCakeAmount;
    }

    public void setFreeCakeAmount(BigDecimal freeCakeAmount) {
        this.freeCakeAmount = freeCakeAmount;
    }

    public Integer getDiscountAppliedPercent() {
        return discountAppliedPercent;
    }

    public void setDiscountAppliedPercent(Integer discountAppliedPercent) {
        this.discountAppliedPercent = discountAppliedPercent;
    }

    public Integer getLifetimeDiscountAppliedPercent() {
        return lifetimeDiscountAppliedPercent;
    }

    public void setLifetimeDiscountAppliedPercent(Integer lifetimeDiscountAppliedPercent) {
        this.lifetimeDiscountAppliedPercent = lifetimeDiscountAppliedPercent;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getTotalSinDescuento() {
        return totalSinDescuento;
    }

    public void setTotalSinDescuento(BigDecimal totalSinDescuento) {
        this.totalSinDescuento = totalSinDescuento;
    }

    public BigDecimal getTotalConDescuento() {
        return totalConDescuento;
    }

    public void setTotalConDescuento(BigDecimal totalConDescuento) {
        this.totalConDescuento = totalConDescuento;
    }

    public String getPurchaserRun() {
        return purchaserRun;
    }

    public void setPurchaserRun(String purchaserRun) {
        this.purchaserRun = purchaserRun;
    }

    public String getPurchaserNombre() {
        return purchaserNombre;
    }

    public void setPurchaserNombre(String purchaserNombre) {
        this.purchaserNombre = purchaserNombre;
    }

    public String getPurchaserApellidos() {
        return purchaserApellidos;
    }

    public void setPurchaserApellidos(String purchaserApellidos) {
        this.purchaserApellidos = purchaserApellidos;
    }

    public String getPurchaserCorreo() {
        return purchaserCorreo;
    }

    public void setPurchaserCorreo(String purchaserCorreo) {
        this.purchaserCorreo = purchaserCorreo;
    }

    public String getPurchaserTelefono() {
        return purchaserTelefono;
    }

    public void setPurchaserTelefono(String purchaserTelefono) {
        this.purchaserTelefono = purchaserTelefono;
    }

    public List<OrderItemSummaryDto> getItems() {
        return items;
    }

    public void setItems(List<OrderItemSummaryDto> items) {
        this.items = items;
    }
}
