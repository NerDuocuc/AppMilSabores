package com.service.pasteleriamilsabores.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.pasteleriamilsabores.models.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {
    List<Address> findByUserRun(String userRun);
}
