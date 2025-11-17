package com.ordersimulation.ordersimulation.dto.order;

import java.math.BigDecimal;
import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class OrderResponse {
    private Long id;
    private String customerName;
    private String address;
    private BigDecimal totalPrice;
    private List<ItemDTO> items;

    @Data @Builder
    public static class ItemDTO {
        private Long id;
        private ProductResponse product;
        private Integer quantity;
        private BigDecimal price;
        private BigDecimal totalPrice;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductResponse {
        private Long id;
        private String name;
        private String type;
    }
}
