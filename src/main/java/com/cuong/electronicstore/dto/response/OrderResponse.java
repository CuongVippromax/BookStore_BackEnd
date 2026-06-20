package com.cuong.electronicstore.dto.response;

import com.cuong.electronicstore.common.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Long id;
    private String orderCode;
    private Long userId;
    private String receiverName;
    private String receiverPhone;
    private String shippingAddress;
    private String note;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private List<OrderItemResponse> items;
    private PaymentResponse payment;
    private Date createdAt;
}
