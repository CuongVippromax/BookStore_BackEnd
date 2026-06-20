package com.cuong.electronicstore.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileRequest {
    private String username;

    @Size(min = 10, max = 10, message = "phone must be 10 digits")
    private String phone;

    private String address;
}
