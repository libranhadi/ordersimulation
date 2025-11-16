package com.ordersimulation.ordersimulation.repository;

import com.ordersimulation.ordersimulation.model.*;


import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p JOIN FETCH p.category JOIN FETCH p.seller WHERE p.deletedAt IS NULL")
    Page<Product> findAllActiveWithRelations(Pageable pageable);
}

