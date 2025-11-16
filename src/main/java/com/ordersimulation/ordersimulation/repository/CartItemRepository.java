package com.ordersimulation.ordersimulation.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ordersimulation.ordersimulation.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("SELECT COUNT(ci) FROM CartItem ci WHERE ci.cart.id = :cartId AND ci.deletedAt IS NULL")
    long countActiveItems(Long cartId);
}
