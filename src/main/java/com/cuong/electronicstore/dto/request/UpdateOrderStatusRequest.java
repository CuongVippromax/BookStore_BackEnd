package com.cuong.electronicstore.dto.request;

import com.cuong.electronicstore.common.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderStatusRequest {
    @NotNull
    private OrderStatus status;
}
