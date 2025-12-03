package com.service.pasteleriamilsabores.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.pasteleriamilsabores.models.Receta;

@Repository
public interface RecetaRepository extends JpaRepository<Receta, String> {

}
