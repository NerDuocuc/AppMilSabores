package com.service.pasteleriamilsabores.dto;

import java.util.ArrayList;
import java.util.List;

public class CartDto {
    private String id;
    private String userRun;
    private List<CartItemDto> items = new ArrayList<>();
    private Integer total;

    public CartDto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserRun() {
        return userRun;
    }

    public void setUserRun(String userRun) {
        this.userRun = userRun;
    }

    public List<CartItemDto> getItems() {
        return items;
    }

    public void setItems(List<CartItemDto> items) {
        this.items = items;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
