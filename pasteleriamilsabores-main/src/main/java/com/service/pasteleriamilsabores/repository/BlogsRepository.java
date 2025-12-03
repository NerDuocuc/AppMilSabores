package com.service.pasteleriamilsabores.repository;

/**
 * Deprecated JSON-backed repository stub.
 * Replaced by JPA `BlogRepository`. Kept as a stub to avoid missing-file errors.
 */
@Deprecated
public final class BlogsRepository {

    private BlogsRepository() {
        // utility - do not instantiate
    }

    @Deprecated
    public static void removed() {
        throw new UnsupportedOperationException("BlogsRepository removed — use com.service.repository.BlogRepository (JpaRepository) instead.");
    }
}
