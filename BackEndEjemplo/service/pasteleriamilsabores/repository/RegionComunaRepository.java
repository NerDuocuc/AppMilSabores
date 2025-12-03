package com.service.pasteleriamilsabores.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.pasteleriamilsabores.models.RegionComuna;

@Repository
public interface RegionComunaRepository extends JpaRepository<RegionComuna, String> {

}
