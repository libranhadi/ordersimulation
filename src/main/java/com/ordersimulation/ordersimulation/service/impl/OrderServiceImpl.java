package com.ordersimulation.ordersimulation.service.impl;

import com.ordersimulation.ordersimulation.dto.order.OrderRequest;
import com.ordersimulation.ordersimulation.dto.order.OrderResponse;
import com.ordersimulation.ordersimulation.dto.order.OrderResponse.ProductResponse;
import com.ordersimulation.ordersimulation.exception.ResourceNotFoundException;
import com.ordersimulation.ordersimulation.model.Cart;
import com.ordersimulation.ordersimulation.model.CartItem;
import com.ordersimulation.ordersimulation.model.Order;
import com.ordersimulation.ordersimulation.model.OrderItem;
import com.ordersimulation.ordersimulation.model.Product;
import com.ordersimulation.ordersimulation.repository.CartItemRepository;
import com.ordersimulation.ordersimulation.repository.CartRepository;
import com.ordersimulation.ordersimulation.repository.OrderRepository;
import com.ordersimulation.ordersimulation.service.OrderService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        Cart cart = cartRepository.findById(request.getCartId())
            .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        List<CartItem> selectedItems = request.getItems().stream()
                .map(i -> cartItemRepository.findById(i.getCartItemId())
                        .orElseThrow(() -> new ResourceNotFoundException("Cart item not found")))
                .collect(Collectors.toList());

        Order order = new Order();
        order.setCart(cart);
        order.setCustomerName(cart.getCustomerName());
        order.setAddress(cart.getAddress());

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem ci : selectedItems) {
            BigDecimal itemTotal = ci.getPrice().multiply(BigDecimal.valueOf(ci.getQuantity()));

            OrderItem oi = OrderItem.builder()
                    .order(order)
                    .product(ci.getProduct())
                    .quantity(ci.getQuantity())
                    .price(ci.getPrice())
                    .totalPrice(itemTotal)
                    .build();

            orderItems.add(oi);
            total = total.add(itemTotal);
        }

        order.setItems(orderItems);
        order.setTotalPrice(total);

        orderRepository.save(order);
        for (CartItem ci : selectedItems) {
            ci.setDeletedAt(LocalDateTime.now());
            cartItemRepository.save(ci);
        }

        long activeItems = cartItemRepository.countActiveItems(cart.getId());
        if (activeItems == 0) {
            cart.setDeletedAt(LocalDateTime.now());
            cartRepository.save(cart);
        }

        return mapToResponse(order);
    }

    private OrderResponse mapToResponse(Order o) {
        return OrderResponse.builder()
                .id(o.getId())
                .customerName(o.getCustomerName())
                .address(o.getAddress())
                .totalPrice(o.getTotalPrice())
                .items(
                        o.getItems().stream().map(i ->
                                OrderResponse.ItemDTO.builder()
                                        .id(i.getId())
                                        .product(mapProduct(i.getProduct()))
                                        .quantity(i.getQuantity())
                                        .price(i.getPrice())
                                        .totalPrice(i.getTotalPrice())
                                        .build()
                        ).toList()
                )
                .build();
    }

    @Override
    public OrderResponse getOrderById(Long id) {
         Order order = orderRepository.findWithOrderItemsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        return mapToResponse(order);
    }

    private OrderResponse.ProductResponse mapProduct(Product p) {
        if (p == null) return null;

        return OrderResponse.ProductResponse.builder()
                .id(p.getId())
                .name(p.getName())
                .type(p.getCategory().getName())
                .build();
    }

}
