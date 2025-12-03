package com.service.pasteleriamilsabores.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.pasteleriamilsabores.models.RegionComuna;
import com.service.pasteleriamilsabores.repository.RegionComunaRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class RegionesComunasService {

    @Autowired
    private RegionComunaRepository regionComunaRepository;


    public List<RegionComuna> listarRegiones() {
        return regionComunaRepository.findAll();
    }

    // backward-compatible delegate used by existing controllers
    public List<RegionComuna> getAll() {
        return listarRegiones();
    }

    public RegionComuna buscarPorId(String id) {
        return regionComunaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Región no encontrada con id: " + id));
    }
}
