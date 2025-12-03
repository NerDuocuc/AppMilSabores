package com.service.pasteleriamilsabores.dto;

public class AddressDto {
    private String id;
    private String address;
    private String region;
    private String comuna;

    public AddressDto() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getComuna() { return comuna; }
    public void setComuna(String comuna) { this.comuna = comuna; }
}
