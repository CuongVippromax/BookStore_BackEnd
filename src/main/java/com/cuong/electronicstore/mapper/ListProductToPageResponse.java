package com.cuong.electronicstore.mapper;

import com.cuong.electronicstore.dto.response.PageResponse;
import com.cuong.electronicstore.dto.response.ProductResponse;
import com.cuong.electronicstore.model.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public class ListProductToPageResponse {
    public static PageResponse<List<ProductResponse>> getProductPageResponse(int page, int size, Page<Product> productEntities) {
        List<ProductResponse> list = productEntities.stream().map(product ->
                ProductResponse.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .brand(product.getBrand())
                        .description(product.getDescription())
                        .sku(product.getSku())
                        .model(product.getModel())
                        .image(product.getImage())
                        .price(product.getPrice())
                        .stock(product.getStock())
                        .soldQuantity(product.getSoldQuantity())
                        .warrantyMonths(product.getWarrantyMonths())
                        .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
                        .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                        .build()
        ).toList();
        return PageResponse.<List<ProductResponse>>builder()
                .size(size)
                .page(page)
                .totalPage(productEntities.getTotalPages())
                .totalElements(productEntities.getTotalElements())
                .data(list)
                .build();
    }
}
