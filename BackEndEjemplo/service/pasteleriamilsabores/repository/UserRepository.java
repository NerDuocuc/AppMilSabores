package com.service.pasteleriamilsabores.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.pasteleriamilsabores.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

	java.util.Optional<User> findByCorreo(String correo);
	boolean existsByCorreoIgnoreCase(String correo);
	boolean existsByCorreoIgnoreCaseAndRunNot(String correo, String run);

}
