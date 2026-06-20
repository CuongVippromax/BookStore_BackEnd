package com.cuong.electronicstore.controller;

import com.cuong.electronicstore.dto.request.AddToCartRequest;
import com.cuong.electronicstore.dto.request.UpdateCartItemRequest;
import com.cuong.electronicstore.dto.response.ApiResponse;
import com.cuong.electronicstore.dto.response.CartResponse;
import com.cuong.electronicstore.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart")
public class CartController {
    private final CartService cartService;

    @GetMapping
    public ApiResponse<CartResponse> getMyCart() {
        return ApiResponse.<CartResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Get cart successfully")
                .data(cartService.getMyCart())
                .build();
    }

    @PostMapping("/items")
    public ApiResponse<CartResponse> addItem(@Valid @RequestBody AddToCartRequest request) {
        return ApiResponse.<CartResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Add to cart successfully")
                .data(cartService.addItem(request))
                .build();
    }

    @PutMapping("/items/{itemId}")
    public ApiResponse<CartResponse> updateItem(@PathVariable Long itemId,
                                                @Valid @RequestBody UpdateCartItemRequest request) {
        return ApiResponse.<CartResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Cart item updated successfully")
                .data(cartService.updateItem(itemId, request))
                .build();
    }

    @DeleteMapping("/items/{itemId}")
    public ApiResponse<CartResponse> removeItem(@PathVariable Long itemId) {
        return ApiResponse.<CartResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Cart item removed successfully")
                .data(cartService.removeItem(itemId))
                .build();
    }

    @DeleteMapping
    public ApiResponse<Void> clear() {
        cartService.clear();
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Cart cleared successfully")
                .build();
    }
}
