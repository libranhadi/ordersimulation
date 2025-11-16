package com.ordersimulation.ordersimulation.service;

import com.ordersimulation.ordersimulation.dto.cart.CartRequest;
import com.ordersimulation.ordersimulation.dto.cart.CartResponse;

public interface CartService {
    CartResponse createCart(CartRequest request);
    CartResponse getCart(Long id);
}
