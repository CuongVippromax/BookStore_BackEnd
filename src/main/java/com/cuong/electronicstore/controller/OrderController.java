package com.cuong.electronicstore.controller;

import com.cuong.electronicstore.common.OrderStatus;
import com.cuong.electronicstore.dto.request.CreateOrderRequest;
import com.cuong.electronicstore.dto.request.UpdateOrderStatusRequest;
import com.cuong.electronicstore.dto.response.ApiResponse;
import com.cuong.electronicstore.dto.response.OrderResponse;
import com.cuong.electronicstore.dto.response.PageResponse;
import com.cuong.electronicstore.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/checkout")
    public ApiResponse<OrderResponse> checkout(@Valid @RequestBody CreateOrderRequest request) {
        return ApiResponse.<OrderResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Order created successfully")
                .data(orderService.checkout(request))
                .build();
    }

    @GetMapping("/me")
    public ApiResponse<PageResponse<List<OrderResponse>>> getMyOrders(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<List<OrderResponse>>>builder()
                .code(HttpStatus.OK.value())
                .message("Get my orders successfully")
                .data(orderService.getMyOrders(page, size))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<OrderResponse> getById(@PathVariable Long id) {
        return ApiResponse.<OrderResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Get order successfully")
                .data(orderService.getById(id))
                .build();
    }

    @PatchMapping("/{id}/cancel")
    public ApiResponse<OrderResponse> cancel(@PathVariable Long id) {
        return ApiResponse.<OrderResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Order cancelled successfully")
                .data(orderService.cancel(id))
                .build();
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','ROLE_ADMIN')")
    public ApiResponse<PageResponse<List<OrderResponse>>> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) OrderStatus status) {
        return ApiResponse.<PageResponse<List<OrderResponse>>>builder()
                .code(HttpStatus.OK.value())
                .message("Get all orders successfully")
                .data(orderService.getAll(page, size, status))
                .build();
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyAuthority('ADMIN','ROLE_ADMIN')")
    public ApiResponse<OrderResponse> updateStatus(@PathVariable Long id,
                                                   @Valid @RequestBody UpdateOrderStatusRequest request) {
        return ApiResponse.<OrderResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Order status updated successfully")
                .data(orderService.updateStatus(id, request.getStatus()))
                .build();
    }
}
