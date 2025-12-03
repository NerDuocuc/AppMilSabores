package com.service.pasteleriamilsabores.repository;

/**
 * Deprecated JSON-backed repository stub.
 * Replaced by JPA `ProductoRepository`.
 */
@Deprecated
public final class ProductosRepository {

    private ProductosRepository() {
    }

    @Deprecated
    public static void removed() {
        throw new UnsupportedOperationException("ProductosRepository removed — use com.service.repository.ProductoRepository (JpaRepository) instead.");
    }
}
