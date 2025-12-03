package com.service.pasteleriamilsabores.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.pasteleriamilsabores.dto.ProductoDto;
import com.service.pasteleriamilsabores.service.ProductosService;

@RestController
@RequestMapping("/api/productos")
public class ProductosController {

    private final ProductosService productosService;

    public ProductosController(ProductosService productosService) {
        this.productosService = productosService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductoDto>> getAll() {
        return ResponseEntity.ok(productosService.listarProductos());
    }

    @GetMapping(path = "{codigo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductoDto> getByCodigo(@PathVariable("codigo") String codigo) {
        return ResponseEntity.ok(productosService.buscarPorCodigo(codigo));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductoDto> create(@RequestBody ProductoDto dto) {
        ProductoDto created = productosService.crearProducto(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping(path = "{codigo}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductoDto> update(@PathVariable("codigo") String codigo, @RequestBody ProductoDto dto) {
        return ResponseEntity.ok(productosService.actualizarProducto(codigo, dto));
    }

    @DeleteMapping(path = "{codigo}")
    public ResponseEntity<Void> delete(@PathVariable("codigo") String codigo) {
        productosService.eliminarProducto(codigo);
        return ResponseEntity.noContent().build();
    }
}
