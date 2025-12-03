package com.service.pasteleriamilsabores.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.pasteleriamilsabores.dto.CategoriaDto;
import com.service.pasteleriamilsabores.models.Categoria;
import com.service.pasteleriamilsabores.repository.CategoriaRepository;

@RestController
@RequestMapping("/api")
public class CategoriasController {

    private final CategoriaRepository categoriaRepository;

    public CategoriasController(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @GetMapping("/categorias")
    public List<CategoriaDto> listarCategorias() {
        return categoriaRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private CategoriaDto toDto(Categoria c) {
        String slug = slugify(c.getNombreCategoria());
        return new CategoriaDto(c.getIdCategoria(), c.getNombreCategoria(), slug);
    }

    private String slugify(String input) {
        if (input == null) return null;
        String normalized = java.text.Normalizer.normalize(input, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        String slug = normalized.toLowerCase().replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
        return slug;
    }
}