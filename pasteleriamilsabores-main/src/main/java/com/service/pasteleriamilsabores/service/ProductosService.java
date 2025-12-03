package com.service.pasteleriamilsabores.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.service.pasteleriamilsabores.dto.ProductoDto;
import com.service.pasteleriamilsabores.models.Producto;
import com.service.pasteleriamilsabores.models.Categoria;
import com.service.pasteleriamilsabores.repository.ProductoRepository;
import com.service.pasteleriamilsabores.repository.CategoriaRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductosService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductosService(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductoDto> listarProductos() {
        return productoRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductoDto buscarPorCodigo(String codigoProducto) {
        return productoRepository.findById(codigoProducto)
                .map(this::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con codigo: " + codigoProducto));
    }

    @Transactional
    public ProductoDto crearProducto(ProductoDto dto) {
        if (dto.getCodigoProducto() == null || dto.getCodigoProducto().isBlank()) {
            throw new IllegalArgumentException("Codigo de producto requerido");
        }
        if (productoRepository.existsById(dto.getCodigoProducto())) {
            throw new IllegalArgumentException("Producto ya existe con codigo: " + dto.getCodigoProducto());
        }
        Producto entity = fromDto(dto);
        return toDto(productoRepository.save(entity));
    }

    @Transactional
    public ProductoDto actualizarProducto(String codigoProducto, ProductoDto dto) {
        Producto existing = productoRepository.findById(codigoProducto)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con codigo: " + codigoProducto));
        applyDto(existing, dto);
        existing.setCodigoProducto(codigoProducto);
        return toDto(productoRepository.save(existing));
    }

    @Transactional
    public void eliminarProducto(String codigoProducto) {
        Producto existing = productoRepository.findById(codigoProducto)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con codigo: " + codigoProducto));
        productoRepository.delete(existing);
    }

    private ProductoDto toDto(Producto producto) {
        ProductoDto dto = new ProductoDto();
        dto.setCodigoProducto(producto.getCodigoProducto());
        dto.setNombreProducto(producto.getNombreProducto());
        dto.setPrecioProducto(producto.getPrecioProducto());
        dto.setDescripcionProducto(producto.getDescripcionProducto());
        // Exponer el nombre base en `imagen_producto` y una URL relativa en `imagen_url`.
        // El cliente puede usar la URL completa anteponiendo el host (p.ej. http://10.0.2.2:8080)
        String imgRaw = producto.getImagenProducto(); // may contain path or filename
        String imgBase = extractBaseName(imgRaw); // ensure we only use the base name
        if (imgBase != null && !imgBase.isEmpty()) {
            // Normalize variants: turn hyphens to underscores, remove accents (ñ->n),
            // replace any non-alphanumeric/underscore with underscore, collapse duplicates
            String normalized = imgBase.replace('-', '_');
            normalized = java.text.Normalizer.normalize(normalized, java.text.Normalizer.Form.NFD)
                    .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
            // Keep only a-z, 0-9 and underscore; replace others with underscore
            normalized = normalized.toLowerCase().replaceAll("[^a-z0-9_]+", "_");
            // Collapse multiple underscores and trim leading/trailing underscores
            normalized = normalized.replaceAll("_+", "_").replaceAll("(^_|_$)", "");
            dto.setImagenProducto(normalized);
            dto.setImagenUrl("/images/products/" + normalized);
        } else {
            dto.setImagenProducto(null);
            dto.setImagenUrl(null);
        }
        dto.setStock(producto.getStock());
        dto.setStockCritico(producto.getStockCritico());
        if (producto.getCategoria() != null) {
            Categoria c = producto.getCategoria();
            dto.setCategoriaId(c.getIdCategoria());
            dto.setCategoriaNombre(c.getNombreCategoria());
            dto.setCategoriaSlug(slugify(c.getNombreCategoria()));
        }
        return dto;
    }

    private Producto fromDto(ProductoDto dto) {
        Producto producto = new Producto();
        applyDto(producto, dto);
        producto.setCodigoProducto(dto.getCodigoProducto());
        return producto;
    }

    private void applyDto(Producto producto, ProductoDto dto) {
        if (dto.getNombreProducto() != null) {
            producto.setNombreProducto(dto.getNombreProducto());
        }
        producto.setPrecioProducto(dto.getPrecioProducto());
        producto.setDescripcionProducto(dto.getDescripcionProducto());
        // Normalize incoming image value: store only base name (no path, no extension)
        if (dto.getImagenProducto() != null) {
            producto.setImagenProducto(extractBaseName(dto.getImagenProducto()));
        }
        producto.setStock(dto.getStock());
        producto.setStockCritico(dto.getStockCritico());
        // assign categoria by id if provided
        if (dto.getCategoriaId() != null) {
            Integer catId = dto.getCategoriaId();
            Categoria cat = categoriaRepository.findById(catId).orElse(null);
            producto.setCategoria(cat);
        }
    }

    // simple slugify: lowercase, remove accents, replace non-alnum with '-'
    private String slugify(String input) {
        if (input == null) return null;
        String normalized = java.text.Normalizer.normalize(input, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        String slug = normalized.toLowerCase().replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
        return slug;
    }

    // Helper: given values like "/images/products/foo.png" or "foo.png" or "foo",
    // return the base name without extension (e.g. "foo").
    private String extractBaseName(String raw) {
        if (raw == null) return null;
        String s = raw.trim();
        int lastSlash = s.lastIndexOf('/');
        if (lastSlash >= 0 && lastSlash < s.length() - 1) {
            s = s.substring(lastSlash + 1);
        }
        int dot = s.lastIndexOf('.');
        if (dot > 0) {
            s = s.substring(0, dot);
        }
        return s;
    }
}