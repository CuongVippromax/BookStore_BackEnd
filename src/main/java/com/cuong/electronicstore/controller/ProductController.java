package com.cuong.electronicstore.controller;

import com.cuong.electronicstore.dto.request.ProductRequest;
import com.cuong.electronicstore.dto.response.ApiResponse;
import com.cuong.electronicstore.dto.response.PageResponse;
import com.cuong.electronicstore.dto.response.ProductResponse;
import com.cuong.electronicstore.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j(topic = "PRODUCT-CONTROLLER")
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ApiResponse<PageResponse<List<ProductResponse>>> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sort) {
        return ApiResponse.<PageResponse<List<ProductResponse>>>builder()
                .code(HttpStatus.OK.value())
                .message("Get all products successfully")
                .data(productService.findAllProduct(page, size, keyword, sort))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> getById(@PathVariable Long id) {
        return ApiResponse.<ProductResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Get product successfully")
                .data(productService.getProductById(id))
                .build();
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','ROLE_ADMIN')")
    public ApiResponse<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
        return ApiResponse.<ProductResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Product created successfully")
                .data(productService.addProduct(request))
                .build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ROLE_ADMIN')")
    public ApiResponse<ProductResponse> update(@PathVariable Long id, @RequestBody ProductRequest request) {
        return ApiResponse.<ProductResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Product updated successfully")
                .data(productService.updateProduct(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ROLE_ADMIN')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        productService.deleteProductById(id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Product deleted successfully")
                .build();
    }
}
