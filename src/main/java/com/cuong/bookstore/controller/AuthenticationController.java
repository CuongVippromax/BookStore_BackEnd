package com.cuong.bookstore.controller;

import com.cuong.bookstore.dto.request.LoginRequest;
import com.cuong.bookstore.dto.response.ApiResponse;
import com.cuong.bookstore.model.User;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ApiResponse<LoginRequest> login (LoginRequest loginRequest) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
        Authentication authenticate = authenticationManager.authenticate(token);
        User user = (User) authenticate.getPrincipal();

        return ApiResponse.<LoginRequest>builder()
                .code(HttpStatus.OK.value())
                .message("Login successful!")
                .data()
                .build();
    }
}
