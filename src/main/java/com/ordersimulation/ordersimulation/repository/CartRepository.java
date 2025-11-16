package com.ordersimulation.ordersimulation.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ordersimulation.ordersimulation.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByCustomerNameAndDeletedAtIsNull(String customerName);
}
