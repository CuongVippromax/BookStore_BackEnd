package com.cuong.bookstore.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreationRequest {
    @NotBlank
    private String username;
    @Size(min = 5)
    private String password;
    @Email
    private String email;
    @Size(min = 5)
    private String address;
    @Size(min = 10 , max = 10)
    private String phone;
}
