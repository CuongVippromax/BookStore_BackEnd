package com.cuong.electronicstore.dto.response;

import com.cuong.electronicstore.common.PaymentMethod;
import com.cuong.electronicstore.common.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {
    private Long id;
    private Long orderId;
    private PaymentMethod method;
    private PaymentStatus status;
    private BigDecimal amount;
    private Date paidAt;
}
