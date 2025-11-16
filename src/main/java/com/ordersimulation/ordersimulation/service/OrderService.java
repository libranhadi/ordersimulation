package com.ordersimulation.ordersimulation.service;

import com.ordersimulation.ordersimulation.dto.order.OrderRequest;
import com.ordersimulation.ordersimulation.dto.order.OrderResponse;


public interface OrderService {
    OrderResponse createOrder(OrderRequest request);
    OrderResponse getOrderById(Long id);
}
