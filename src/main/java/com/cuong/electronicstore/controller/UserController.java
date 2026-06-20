package com.cuong.electronicstore.controller;

import com.cuong.electronicstore.dto.request.UpdateProfileRequest;
import com.cuong.electronicstore.dto.response.ApiResponse;
import com.cuong.electronicstore.dto.response.UserResponse;
import com.cuong.electronicstore.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j(topic = "USER-CONTROLLER")
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ApiResponse<UserResponse> getMe() {
        return ApiResponse.<UserResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Get current user successfully")
                .data(userService.getMe())
                .build();
    }

    @PatchMapping("/me")
    public ApiResponse<UserResponse> updateMe(@Valid @RequestBody UpdateProfileRequest request) {
        return ApiResponse.<UserResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Profile updated successfully")
                .data(userService.updateMe(request))
                .build();
    }
}
