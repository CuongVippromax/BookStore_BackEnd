package com.cuong.bookstore.mapper;

import com.cuong.bookstore.dto.response.PageResponse;
import com.cuong.bookstore.dto.response.UserResponse;
import com.cuong.bookstore.model.User;
import lombok.*;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Setter
@Builder
@Service
public class ListUserToPageResponse {
    public static PageResponse getUserPageResponse(int page, int size , Page<User> userEntities){
        List<UserResponse> list = userEntities.stream().map(user ->
                UserResponse.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .phone(user.getPhone())
                        .email(user.getEmail())
                        .address(user.getAddress())
                        .build()
        ).toList();

        PageResponse<List<UserResponse>> userPageResponse = PageResponse.<List<UserResponse>>builder()
                .page(page)
                .size(size)
                .totalPage(userEntities.getTotalPages())
                .totalElements((int) userEntities.getTotalElements())
                .build();
        return userPageResponse;
    }
}
