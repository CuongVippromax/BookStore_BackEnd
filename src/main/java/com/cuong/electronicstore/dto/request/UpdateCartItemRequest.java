package com.cuong.electronicstore.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCartItemRequest {
    @NotNull
    @Min(value = 1, message = "quantity must be >= 1")
    private Integer quantity;
}
