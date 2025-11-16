package com.ordersimulation.ordersimulation.dto.cart;

import lombok.Data;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;

@Data
public class CartRequest {
    @NotBlank(message = "customer name is required")
    private String customerName;
    @NotBlank(message = "address is required")
    private String address;

    @NotEmpty(message = "Items cannot be empty")
    private List<@Valid CartItemRequest> items;

    @Data
    public static class CartItemRequest {
        @NotNull(message = "Product ID is required")
        private Long productId;
        @NotNull(message = "quantity is required")
        @Min(value = 1, message = "Quantity must be greater than 0")
        private Integer quantity;
    }
}
