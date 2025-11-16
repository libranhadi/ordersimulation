package com.ordersimulation.ordersimulation.service.impl;

import com.ordersimulation.ordersimulation.dto.cart.*;
import com.ordersimulation.ordersimulation.exception.ResourceNotFoundException;
import com.ordersimulation.ordersimulation.model.*;
import com.ordersimulation.ordersimulation.repository.*;
import com.ordersimulation.ordersimulation.service.CartService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Override
    public CartResponse createCart(CartRequest request) {
        Cart cart = cartRepository.findByCustomerNameAndDeletedAtIsNull(request.getCustomerName())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setCustomerName(request.getCustomerName());
                    newCart.setAddress(request.getAddress());
                    newCart.setTotalPrice(BigDecimal.ZERO);
                    return newCart;
                });

        if (cart.getItems() == null) {
            cart.setItems(new ArrayList<>());
        }

        for (var itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

            CartItem existingItem = cart.getItems().stream()
                    .filter(i -> i.getProduct().getId().equals(product.getId()))
                    .findFirst()
                    .orElse(null);

            BigDecimal price = product.getPrice(); // price = BigDecimal
            BigDecimal qty = BigDecimal.valueOf(itemReq.getQuantity());

            if (existingItem != null) {
                int newQty = existingItem.getQuantity() + itemReq.getQuantity();
                existingItem.setQuantity(newQty);
                existingItem.setTotalPrice(price.multiply(BigDecimal.valueOf(newQty)));

            } else {
                CartItem newItem = CartItem.builder()
                        .product(product)
                        .quantity(itemReq.getQuantity())
                        .price(price)
                        .totalPrice(price.multiply(qty))
                        .cart(cart)
                        .build();

                cart.getItems().add(newItem);
            }
        }

        // hitung total cart
        BigDecimal totalPrice = cart.getItems().stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalPrice(totalPrice);

        cartRepository.save(cart);
        return mapToResponse(cart);
    }

    @Override
    public CartResponse getCart(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        return mapToResponse(cart);
    }

    private CartResponse mapToResponse(Cart cart) {
        return CartResponse.builder()
                .id(cart.getId())
                .customerName(cart.getCustomerName())
                .address(cart.getAddress())
                .totalPrice(cart.getTotalPrice())
                .items(
                        cart.getItems().stream().map(i ->
                                CartResponse.CartItemResponse.builder()
                                        .id(i.getId())
                                        .productId(i.getProduct().getId())
                                        .productName(i.getProduct().getName())
                                        .type(i.getProduct().getCategory().getName())
                                        .quantity(i.getQuantity())
                                        .price(i.getPrice())
                                        .totalPrice(i.getTotalPrice())
                                        .build()
                        ).collect(Collectors.toList())
                )
                .build();
    }
}
