package com.cuong.electronicstore.controller;

import com.cuong.electronicstore.dto.request.ReviewRequest;
import com.cuong.electronicstore.dto.response.ApiResponse;
import com.cuong.electronicstore.dto.response.PageResponse;
import com.cuong.electronicstore.dto.response.ReviewResponse;
import com.cuong.electronicstore.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/product/{productId}")
    public ApiResponse<PageResponse<List<ReviewResponse>>> getByProduct(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<List<ReviewResponse>>>builder()
                .code(HttpStatus.OK.value())
                .message("Get reviews successfully")
                .data(reviewService.getByProduct(productId, page, size))
                .build();
    }

    @PostMapping
    public ApiResponse<ReviewResponse> create(@Valid @RequestBody ReviewRequest request) {
        return ApiResponse.<ReviewResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Review created successfully")
                .data(reviewService.create(request))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<ReviewResponse> update(@PathVariable Long id, @Valid @RequestBody ReviewRequest request) {
        return ApiResponse.<ReviewResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Review updated successfully")
                .data(reviewService.update(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        reviewService.delete(id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Review deleted successfully")
                .build();
    }
}
