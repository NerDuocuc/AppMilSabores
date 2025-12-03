package com.service.pasteleriamilsabores.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.pasteleriamilsabores.models.Receta;
import com.service.pasteleriamilsabores.repository.RecetaRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class RecetasService {

    @Autowired
    private RecetaRepository recetaRepository;


    public List<Receta> listarRecetas() {
        return recetaRepository.findAll();
    }

    // backward-compatible delegate used by existing controllers
    public List<Receta> getAll() {
        return listarRecetas();
    }

    public Receta buscarPorId(String id) {
        return recetaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Receta no encontrada con id: " + id));
    }
}
