package com.cuong.electronicstore.controller;

import com.cuong.electronicstore.dto.response.ApiResponse;
import com.cuong.electronicstore.dto.response.PaymentResponse;
import com.cuong.electronicstore.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping("/order/{orderId}")
    public ApiResponse<PaymentResponse> getByOrder(@PathVariable Long orderId) {
        return ApiResponse.<PaymentResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Get payment successfully")
                .data(paymentService.getByOrderId(orderId))
                .build();
    }
}
