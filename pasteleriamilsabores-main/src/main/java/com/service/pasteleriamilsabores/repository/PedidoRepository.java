package com.service.pasteleriamilsabores.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.pasteleriamilsabores.models.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, String> {
	List<Pedido> findByUserRunOrderByCreatedAtDesc(String run);
	List<Pedido> findAllByOrderByCreatedAtDesc();
}
