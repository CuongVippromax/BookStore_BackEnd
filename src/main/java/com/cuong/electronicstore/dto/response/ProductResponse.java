package com.cuong.electronicstore.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String brand;
    private String description;
    private String sku;
    private String model;
    private String image;
    private BigDecimal price;
    private Integer stock;
    private Integer soldQuantity;
    private Integer warrantyMonths;
    private Integer categoryId;
    private String categoryName;
    private Double averageRating;
    private Long totalReviews;
}
