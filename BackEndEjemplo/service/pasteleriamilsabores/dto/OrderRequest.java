package com.service.pasteleriamilsabores.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderRequest {
    private String userRun;
    private List<OrderItemRequest> items;
    private String deliveryAddress;
    private Boolean applyDiscounts; // if true, apply discounts (only to tortas)

    @JsonProperty("run")
    public void setRun(String run) {
        this.userRun = run;
    }

    public String getUserRun() { return userRun; }
    public void setUserRun(String userRun) { this.userRun = userRun; }
    public List<OrderItemRequest> getItems() { return items; }
    public void setItems(List<OrderItemRequest> items) { this.items = items; }
    public String getDeliveryAddress() { return deliveryAddress; }
    @JsonProperty("direccionEntrega")
    public void setDireccionEntrega(String direccionEntrega) { this.deliveryAddress = direccionEntrega; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    public Boolean getApplyDiscounts() { return applyDiscounts; }
    public void setApplyDiscounts(Boolean applyDiscounts) { this.applyDiscounts = applyDiscounts; }
}
