package com.ordersimulation.ordersimulation.dto.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.ordersimulation.ordersimulation.model.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private CategoryDTO category;
    private SellerDTO seller;
    private LocalDateTime createdAt;

    @Data
    @Builder
    public static class CategoryDTO {
        private Long id;
        private String name;
        private String code;
    }

    @Data
    @Builder
    public static class SellerDTO {
        private Long id;
        private String name;
    }


    public static ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .category(ProductResponse.CategoryDTO.builder()
                        .id(product.getCategory().getId())
                        .name(product.getCategory().getName())
                        .code(product.getCategory().getCode())
                        .build())
                .seller(ProductResponse.SellerDTO.builder()
                        .id(product.getSeller().getId())
                        .name(product.getSeller().getName())
                        .build())
                .createdAt(product.getCreatedAt())
                .build();
    }
}