package com.service.pasteleriamilsabores.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.pasteleriamilsabores.service.RecetasService;

@RestController
@RequestMapping("/api/recetas")
public class RecetasController {

    private final RecetasService recetasService;

    public RecetasController(RecetasService recetasService) {
        this.recetasService = recetasService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(recetasService.getAll());
    }
}
