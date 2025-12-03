package com.service.pasteleriamilsabores.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.pasteleriamilsabores.models.Card;

@Repository
public interface CardRepository extends JpaRepository<Card, String> {
    List<Card> findByUserRun(String userRun);
}
