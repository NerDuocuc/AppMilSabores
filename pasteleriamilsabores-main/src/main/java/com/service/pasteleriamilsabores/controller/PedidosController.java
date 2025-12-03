package com.service.pasteleriamilsabores.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.pasteleriamilsabores.dto.OrderRequest;
import com.service.pasteleriamilsabores.dto.OrderResponse;
import com.service.pasteleriamilsabores.dto.OrderSummaryDto;
import com.service.pasteleriamilsabores.models.UserType;
import com.service.pasteleriamilsabores.service.PedidosService;
import com.service.pasteleriamilsabores.security.CustomUserDetails;

@RestController
@RequestMapping("/api/pedidos")
public class PedidosController {

    private final PedidosService pedidosService;

    public PedidosController(PedidosService pedidosService) {
        this.pedidosService = pedidosService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listOrders(@AuthenticationPrincipal CustomUserDetails principal) {
        if (principal == null || principal.getUser() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required");
        }
        List<OrderSummaryDto> orders = pedidosService.listOrdersForUser(principal.getUser().getRun());
        return ResponseEntity.ok(orders);
    }

    @GetMapping(value = "/usuario/{run}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listOrdersByRun(@AuthenticationPrincipal CustomUserDetails principal,
                                             @org.springframework.web.bind.annotation.PathVariable("run") String run) {
        if (principal == null || principal.getUser() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required");
        }
        if (run == null || run.isBlank()) {
            return ResponseEntity.badRequest().body("Run is required");
        }
        boolean isOwner = run.equals(principal.getUser().getRun());
        if (!isOwner && !isAdmin(principal)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden");
        }
        List<OrderSummaryDto> orders = pedidosService.listOrdersForUser(run);
        return ResponseEntity.ok(orders);
    }

    @GetMapping(value = "/admin", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listAllOrders(@AuthenticationPrincipal CustomUserDetails principal) {
        if (!isAdmin(principal)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden");
        }
        return ResponseEntity.ok(pedidosService.listAllOrders());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createOrder(@RequestBody OrderRequest req) {
        try {
            OrderResponse resp = pedidosService.createOrder(req);
            return ResponseEntity.status(201).body(resp);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating order");
        }
    }

    private boolean isAdmin(CustomUserDetails principal) {
        if (principal == null || principal.getUser() == null) {
            return false;
        }
        UserType type = principal.getUser().getTipoUsuario();
        return type == UserType.ADMINISTRADOR || type == UserType.SUPER_ADMIN;
    }
}
