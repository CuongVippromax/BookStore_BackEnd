package com.cuong.bookstore.controller;

import com.cuong.bookstore.dto.request.ChangePasswordRequest;
import com.cuong.bookstore.dto.request.UserCreationRequest;
import com.cuong.bookstore.dto.response.ApiResponse;
import com.cuong.bookstore.dto.response.UserResponse;
import com.cuong.bookstore.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j(topic = "USER-CONTROLLER")
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;
    @PostMapping()
    @PreAuthorize("hasAnyAuthority()")
    public ApiResponse<UserResponse> registration(@RequestBody @Valid UserCreationRequest request) {
        UserResponse user = userService.createUser(request);
        return ApiResponse.<UserResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Created User Successfully")
                .data(user)
                .build();
    }
    @GetMapping("/getInfo")
    public ApiResponse<UserResponse> getInfo(@RequestParam String email) {
        UserResponse userByEmail = userService.getUserByEmail(email);
        return ApiResponse.<UserResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Get User Details Successfully")
                .data(userByEmail)
                .build();
    }
    @PatchMapping("/change_psw")
    public ApiResponse<String> changePassword(@RequestBody @Valid ChangePasswordRequest request ) {
        String s = userService.changePassword(request);
        return ApiResponse.<String>builder()
                .code(HttpStatus.OK.value())
                .message("Change Password Successfully")
                .data(s)
                .build();
    }

}
