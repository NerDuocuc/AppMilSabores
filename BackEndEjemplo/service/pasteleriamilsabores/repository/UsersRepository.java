package com.service.pasteleriamilsabores.repository;

/**
 * Deprecated JSON-backed repository stub.
 * Replaced by JPA `UserRepository`.
 */
@Deprecated
public final class UsersRepository {

    private UsersRepository() {
    }

    @Deprecated
    public static void removed() {
        throw new UnsupportedOperationException("UsersRepository removed — use com.service.repository.UserRepository (JpaRepository) instead.");
    }
}
