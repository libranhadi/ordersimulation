package com.ordersimulation.ordersimulation.repository;

import com.ordersimulation.ordersimulation.model.*;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SellerRepository extends JpaRepository<Seller, Long> {
    
    @Query("SELECT s FROM Seller s LEFT JOIN FETCH s.products WHERE s.id = :id")
    Optional<Seller> findByIdWithProducts(@Param("id") Long id);

    Optional<Seller> findByName(String name);
}