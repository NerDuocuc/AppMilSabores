package com.service.pasteleriamilsabores.service;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.service.pasteleriamilsabores.dto.CartDto;
import com.service.pasteleriamilsabores.dto.CartItemDto;
import com.service.pasteleriamilsabores.models.Cart;
import com.service.pasteleriamilsabores.models.CartItem;
import com.service.pasteleriamilsabores.models.Producto;
import com.service.pasteleriamilsabores.repository.CartRepository;
import com.service.pasteleriamilsabores.repository.ProductoRepository;
import com.service.pasteleriamilsabores.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductoRepository productoRepository;

    public CartService(CartRepository cartRepository, UserRepository userRepository, ProductoRepository productoRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productoRepository = productoRepository;
    }

    @Transactional(readOnly = true)
    public CartDto getCart(String userRun) {
        Cart cart = findOrCreateCart(userRun);
        return toDto(cart);
    }

    public CartItemDto addItem(String userRun, CartItemDto request) {
        Cart cart = findOrCreateCart(userRun);
        Producto producto = productoRepository.findById(request.getProductoCodigo())
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado: " + request.getProductoCodigo()));

        CartItem item = findItem(cart, request.getProductoCodigo())
                .orElseGet(() -> createCartItem(cart, request.getProductoCodigo()));

        int requestCantidad = safeInt(request.getCantidad());
        if (requestCantidad <= 0) {
            throw new IllegalArgumentException("Cantidad debe ser mayor a cero");
        }

        int currentCantidad = safeInt(item.getCantidad());
        item.setCantidad(currentCantidad + requestCantidad);
        item.setPrecioUnitario(producto.getPrecioProducto());
        cartRepository.save(cart);
        return toDto(item);
    }

    public CartItemDto updateItem(String userRun, String itemId, CartItemDto request) {
        Cart cart = findOrCreateCart(userRun);
        CartItem item = cart.getItems().stream()
                .filter(ci -> ci.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Item no encontrado: " + itemId));

        int cantidadRequest = safeInt(request.getCantidad());
        if (cantidadRequest <= 0) {
            throw new IllegalArgumentException("Cantidad debe ser mayor a cero");
        }

        item.setCantidad(cantidadRequest);
        cartRepository.save(cart);
        return toDto(item);
    }

    public void removeItem(String userRun, String itemId) {
        Cart cart = findOrCreateCart(userRun);
        CartItem item = cart.getItems().stream()
                .filter(ci -> ci.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Item no encontrado: " + itemId));
        cart.removeItem(item);
        cartRepository.save(cart);
    }

    private Cart findOrCreateCart(String userRun) {
        ensureUserExists(userRun);
        Optional<Cart> optional = cartRepository.findByUserRun(userRun);
        if (optional.isPresent()) {
            return optional.get();
        }
        Cart cart = new Cart();
        cart.setId(UUID.randomUUID().toString());
        cart.setUserRun(userRun);
        return cartRepository.save(cart);
    }

    private Optional<CartItem> findItem(Cart cart, String productCode) {
        return cart.getItems().stream()
                .filter(item -> item.getProductoCodigo().equals(productCode))
                .findFirst();
    }

    private CartItem createCartItem(Cart cart, String productoCodigo) {
        CartItem item = new CartItem();
        item.setId(UUID.randomUUID().toString());
        item.setProductoCodigo(productoCodigo);
        item.setCart(cart);
        cart.addItem(item);
        return item;
    }

    private void ensureUserExists(String run) {
        userRepository.findById(run).orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado: " + run));
    }

    private CartDto toDto(Cart cart) {
        CartDto dto = new CartDto();
        dto.setId(cart.getId());
        dto.setUserRun(cart.getUserRun());
        dto.setItems(cart.getItems().stream().map(this::toDto).collect(Collectors.toList()));
        dto.setTotal(cart.getItems().stream()
            .mapToInt(item -> safeInt(item.getCantidad()) * safeInt(item.getPrecioUnitario()))
            .sum());
        return dto;
    }

    private CartItemDto toDto(CartItem item) {
        CartItemDto dto = new CartItemDto();
        dto.setId(item.getId());
        dto.setProductoCodigo(item.getProductoCodigo());
        int cantidad = safeInt(item.getCantidad());
        int precio = safeInt(item.getPrecioUnitario());
        dto.setCantidad(cantidad);
        dto.setPrecioUnitario(precio);
        dto.setPrecioTotal(cantidad * precio);
        return dto;
    }

    private int safeInt(Integer value) {
        return value != null ? value : 0;
    }
}
