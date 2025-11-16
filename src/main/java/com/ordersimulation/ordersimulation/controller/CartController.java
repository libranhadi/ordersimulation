package com.ordersimulation.ordersimulation.controller;

import com.ordersimulation.ordersimulation.dto.cart.CartRequest;
import com.ordersimulation.ordersimulation.dto.cart.CartResponse;
import com.ordersimulation.ordersimulation.service.CartService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartResponse> createCart(@RequestBody @Valid CartRequest request) {
        return ResponseEntity.ok(cartService.createCart(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartResponse> getCart(@PathVariable Long id) {
        return ResponseEntity.ok(cartService.getCart(id));
    }
}
