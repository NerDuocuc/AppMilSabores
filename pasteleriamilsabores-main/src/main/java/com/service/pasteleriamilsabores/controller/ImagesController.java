package com.service.pasteleriamilsabores.controller;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/images/products")
public class ImagesController {

    private static final String[] EXTENSIONS = { "png", "jpg", "jpeg", "webp", "avif", "gif" };

    @Autowired
    private ResourceLoader resourceLoader;

    @GetMapping("/{name}")
    public ResponseEntity<Resource> getProductImage(@PathVariable("name") String name) {
        try {
            for (String ext : EXTENSIONS) {
                String location = "classpath:static/images/products/" + name + "." + ext;
                Resource res = resourceLoader.getResource(location);
                if (res.exists() && res.isReadable()) {
                    MediaType contentType = detectMediaType(ext);
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(contentType);
                    return new ResponseEntity<>(res, headers, HttpStatus.OK);
                }
            }
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private MediaType detectMediaType(String ext) {
        ext = ext.toLowerCase();
        switch (ext) {
            case "png": return MediaType.IMAGE_PNG;
            case "jpg":
            case "jpeg": return MediaType.IMAGE_JPEG;
            case "webp": return MediaType.parseMediaType("image/webp");
            case "avif": return MediaType.parseMediaType("image/avif");
            case "gif": return MediaType.IMAGE_GIF;
            default: return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}