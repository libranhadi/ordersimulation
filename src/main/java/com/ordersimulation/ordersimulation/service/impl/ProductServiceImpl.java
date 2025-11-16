package com.ordersimulation.ordersimulation.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ordersimulation.ordersimulation.dto.pagination.PaginationResponse;
import com.ordersimulation.ordersimulation.dto.product.ProductRequest;
import com.ordersimulation.ordersimulation.dto.product.ProductResponse;
import com.ordersimulation.ordersimulation.exception.ResourceNotFoundException;
import com.ordersimulation.ordersimulation.model.Category;
import com.ordersimulation.ordersimulation.model.Product;
import com.ordersimulation.ordersimulation.model.Seller;
import com.ordersimulation.ordersimulation.repository.CategoryRepository;
import com.ordersimulation.ordersimulation.repository.ProductRepository;
import com.ordersimulation.ordersimulation.repository.SellerRepository;
import com.ordersimulation.ordersimulation.service.ProductService;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SellerRepository sellerRepository;

    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_LIMIT = 50;
    private static final String DEFAULT_SORT_BY = "createdAt";
    private static final Sort.Direction DEFAULT_SORT_DIRECTION = Sort.Direction.ASC;

    public Product createProduct(ProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Category not found with id: " + request.getCategoryId()
                ));
        
        Seller seller = sellerRepository.findById(request.getSellerId())
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found"));

        Product product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .category(category) 
                .seller(seller)     
                .build();

        return productRepository.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationResponse<ProductResponse> getAllProducts(Integer page, Integer limit) {
        
        Pageable pageable = createPageable(page, limit);
        Page<Product> productPage = productRepository.findAllActiveWithRelations(pageable);
        
        List<ProductResponse> productResponses = productPage.getContent()
                .stream()
                .map(ProductResponse::mapToResponse)
                .collect(Collectors.toList());
        
        Page<ProductResponse> responsePage = productPage.map(ProductResponse::mapToResponse);
        
        return PaginationResponse.create(responsePage);
    }

    private Pageable createPageable(Integer page, Integer limit) {
        int pageNumber = (page == null || page < 1) ? DEFAULT_PAGE : page;
        int pageSize = (limit == null || limit < 1) ? DEFAULT_LIMIT : limit;
        
        int zeroBasedPage = pageNumber - 1;
        
        return PageRequest.of(
            zeroBasedPage, 
            pageSize, 
            Sort.by(DEFAULT_SORT_DIRECTION, DEFAULT_SORT_BY)
        );
    }
}
