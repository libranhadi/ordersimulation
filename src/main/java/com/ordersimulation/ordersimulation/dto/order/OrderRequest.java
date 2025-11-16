package com.ordersimulation.ordersimulation.dto.order;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderRequest {
    @NotNull(message = "cart id is required")
    private Long cartId;

    @NotEmpty(message = "please selected item first")
    private List<SelectedItem> items;

    @Data
    public static class SelectedItem {
        private Long cartItemId;
    }

}