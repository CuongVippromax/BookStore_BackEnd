package com.cuong.electronicstore.service;

import com.cuong.electronicstore.dto.response.PaymentResponse;
import com.cuong.electronicstore.exception.ResourceNotFoundException;
import com.cuong.electronicstore.model.Payment;
import com.cuong.electronicstore.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentResponse getByOrderId(Long orderId) {
        Payment p = paymentRepository.findByOrder_Id(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for order: " + orderId));
        return toResponse(p);
    }

    private PaymentResponse toResponse(Payment p) {
        return PaymentResponse.builder()
                .id(p.getId())
                .orderId(p.getOrder().getId())
                .method(p.getMethod())
                .status(p.getStatus())
                .amount(p.getAmount())
                .paidAt(p.getPaidAt())
                .build();
    }
}
