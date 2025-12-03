package com.service.pasteleriamilsabores.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Entity
    @Table(name = "regiones_comunas")
    public class RegionComuna {
        @Id
        @Column(name = "id")
        private String id;
        @Column(name = "region")
        private String region;
        @ElementCollection
        @CollectionTable(name = "regiones_comunas_comunas", joinColumns = @JoinColumn(name = "region_id"))
        @Column(name = "comuna")
        private List<String> comunas = new ArrayList<>();
    }
