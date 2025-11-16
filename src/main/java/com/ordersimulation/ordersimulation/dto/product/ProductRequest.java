package com.ordersimulation.ordersimulation.dto.product;
import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ProductRequest {
    @NotBlank(message = "product name is required")
    private String name;

    @Min(value = 1, message = "Amount must be greater than 0")
    @Positive(message = "Price must be positive")
    private BigDecimal price;
    
    @NotNull(message = "Category ID is required")
    private Long categoryId;
    
    @NotNull(message = "Seller ID is required")
    private Long sellerId;
}