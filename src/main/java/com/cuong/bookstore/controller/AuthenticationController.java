package com.cuong.bookstore.controller;

import com.cuong.bookstore.dto.request.LoginRequest;
import com.cuong.bookstore.dto.response.ApiResponse;
import com.cuong.bookstore.dto.response.LoginResponse;
import com.cuong.bookstore.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Slf4j
@Valid
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login (@RequestBody @Valid LoginRequest loginRequest) throws AuthenticationException {
        var result = authenticationService.login(loginRequest);

        return ApiResponse.<LoginResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Login successful!")
                .data(result)
                .build();
    }
    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestHeader("Authorization") String authHeader) throws ParseException {
        String token = authHeader.replace("Bearer ", "");
        authenticationService.logout(token);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Logout successful!")
                .build();
    }
}
