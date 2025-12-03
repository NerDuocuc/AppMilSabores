package com.service.pasteleriamilsabores.dto;

import java.math.BigDecimal;
import java.util.List;

public class OrderResponse {
    private String pedidoId;
    private BigDecimal subtotal;
    private BigDecimal freeCakeAmount;
    private Integer discountPercentApplied;
    private Integer lifetimeDiscountPercentApplied;
    private BigDecimal discountAmount;
    private BigDecimal total;
    private List<Object> items; // simple echo or details
    private Boolean freeCakeAvailable;
    private java.util.List<String> freeCakeSuggestedProducts;

    public String getPedidoId() { return pedidoId; }
    public void setPedidoId(String pedidoId) { this.pedidoId = pedidoId; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    public BigDecimal getFreeCakeAmount() { return freeCakeAmount; }
    public void setFreeCakeAmount(BigDecimal freeCakeAmount) { this.freeCakeAmount = freeCakeAmount; }
    public Integer getDiscountPercentApplied() { return discountPercentApplied; }
    public void setDiscountPercentApplied(Integer discountPercentApplied) { this.discountPercentApplied = discountPercentApplied; }
    public Integer getLifetimeDiscountPercentApplied() { return lifetimeDiscountPercentApplied; }
    public void setLifetimeDiscountPercentApplied(Integer lifetimeDiscountPercentApplied) { this.lifetimeDiscountPercentApplied = lifetimeDiscountPercentApplied; }
    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public List<Object> getItems() { return items; }
    public void setItems(List<Object> items) { this.items = items; }
    public Boolean getFreeCakeAvailable() { return freeCakeAvailable; }
    public void setFreeCakeAvailable(Boolean freeCakeAvailable) { this.freeCakeAvailable = freeCakeAvailable; }
    public java.util.List<String> getFreeCakeSuggestedProducts() { return freeCakeSuggestedProducts; }
    public void setFreeCakeSuggestedProducts(java.util.List<String> freeCakeSuggestedProducts) { this.freeCakeSuggestedProducts = freeCakeSuggestedProducts; }
}
