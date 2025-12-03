package com.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

@Service
public class DataService {

    private String readResource(String resourcePath) {
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IllegalArgumentException("Resource not found: " + resourcePath);
            }
            return StreamUtils.copyToString(is, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read resource: " + resourcePath, e);
        }
    }

    public String getRecetas() {
        return readResource("com/service/data/recetas.json");
    }

    public String getBlogs() {
        return readResource("com/service/data/blogs.json");
    }

    public String getProductos() {
        return readResource("com/service/data/products/productos.json");
    }

    public String getRegionesComunas() {
        return readResource("com/service/data/regionesComunas/regionesComunas.json");
    }

    public String getUsers() {
        return readResource("com/service/data/users/users.json");
    }
}
