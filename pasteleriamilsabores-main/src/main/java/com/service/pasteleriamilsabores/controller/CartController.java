package com.service.pasteleriamilsabores.controller;

import java.util.Map;

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

import com.service.pasteleriamilsabores.dto.CartDto;
import com.service.pasteleriamilsabores.dto.CartItemDto;
import com.service.pasteleriamilsabores.service.CartService;

@RestController
@RequestMapping("/api/users/{run}/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CartDto> getCart(@PathVariable("run") String run) {
        return ResponseEntity.ok(cartService.getCart(run));
    }

    @PostMapping(value = "/items", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CartItemDto> addItem(@PathVariable("run") String run, @RequestBody CartItemDto dto) {
        return ResponseEntity.status(201).body(cartService.addItem(run, dto));
    }

    @PutMapping(value = "/items/{itemId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CartItemDto> updateItem(@PathVariable("run") String run,
                                                  @PathVariable("itemId") String itemId,
                                                  @RequestBody CartItemDto dto) {
        return ResponseEntity.ok(cartService.updateItem(run, itemId, dto));
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Map<String, String>> removeItem(@PathVariable("run") String run,
                                                          @PathVariable("itemId") String itemId) {
        cartService.removeItem(run, itemId);
        return ResponseEntity.ok(Map.of("status", "deleted"));
    }
}
