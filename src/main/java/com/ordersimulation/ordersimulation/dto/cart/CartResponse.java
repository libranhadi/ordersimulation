package com.ordersimulation.ordersimulation.dto.cart;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class CartResponse {
    private Long id;
    private String customerName;
    private String address;
    private BigDecimal totalPrice;
    private List<CartItemResponse> items;

    @Data
    @Builder
    public static class CartItemResponse {
        private Long id;
        private Long productId;
        private String productName;
        private String type;
        private Integer quantity;
        private BigDecimal price;
        private BigDecimal totalPrice;
    }
}
