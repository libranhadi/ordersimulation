
package com.ordersimulation.ordersimulation.service;

import com.ordersimulation.ordersimulation.dto.pagination.PaginationResponse;


import com.ordersimulation.ordersimulation.dto.product.ProductRequest;
import com.ordersimulation.ordersimulation.dto.product.ProductResponse;
import com.ordersimulation.ordersimulation.model.Product;

public interface ProductService {
    Product createProduct(ProductRequest request);
    PaginationResponse<ProductResponse> getAllProducts(Integer page, Integer limit);
}
