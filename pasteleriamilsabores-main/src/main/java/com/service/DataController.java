package com.service;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/raw")
public class DataController {

    private final DataService dataService;

    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping(value = "/recetas", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> recetas() {
        return ResponseEntity.ok(dataService.getRecetas());
    }

    @GetMapping(value = "/blogs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> blogs() {
        return ResponseEntity.ok(dataService.getBlogs());
    }

    @GetMapping(value = "/productos", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> productos() {
        return ResponseEntity.ok(dataService.getProductos());
    }

    @GetMapping(value = "/regiones-comunas", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> regionesComunas() {
        return ResponseEntity.ok(dataService.getRegionesComunas());
    }

    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> users() {
        return ResponseEntity.ok(dataService.getUsers());
    }
}
