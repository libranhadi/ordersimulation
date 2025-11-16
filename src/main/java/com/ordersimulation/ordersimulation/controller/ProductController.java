package com.ordersimulation.ordersimulation.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ordersimulation.ordersimulation.dto.pagination.PaginationResponse;
import com.ordersimulation.ordersimulation.dto.product.ProductRequest;
import com.ordersimulation.ordersimulation.dto.product.ProductResponse;
import com.ordersimulation.ordersimulation.model.Product;
import com.ordersimulation.ordersimulation.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
 
    @GetMapping
    public ResponseEntity<PaginationResponse<ProductResponse>> getAllProducts(
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer limit) {
        
        PaginationResponse<ProductResponse> response = productService.getAllProducts(page, limit);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody @Valid ProductRequest request) {
        Product product = productService.createProduct(request);
        ProductResponse response = ProductResponse.mapToResponse(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
