package com.service.pasteleriamilsabores.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.pasteleriamilsabores.models.Blog;
import com.service.pasteleriamilsabores.repository.BlogRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class BlogsService {

    @Autowired
    private BlogRepository blogRepository;


    public List<Blog> listarBlogs() {
        return blogRepository.findAll();
    }

    // backward-compatible delegate used by existing controllers
    public List<Blog> getAll() {
        return listarBlogs();
    }

    public Blog buscarPorId(String id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Blog no encontrado con id: " + id));
    }
}
