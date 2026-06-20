package com.cuong.electronicstore.service;

import com.cuong.electronicstore.dto.request.UpdateProfileRequest;
import com.cuong.electronicstore.dto.response.UserResponse;
import com.cuong.electronicstore.model.User;
import com.cuong.electronicstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;

    public UserResponse getMe() {
        return toResponse(currentUserService.getCurrentUser());
    }

    public UserResponse updateMe(UpdateProfileRequest req) {
        User user = currentUserService.getCurrentUser();
        if (StringUtils.hasLength(req.getUsername())) user.setUsername(req.getUsername());
        if (req.getPhone() != null) user.setPhone(req.getPhone());
        if (req.getAddress() != null) user.setAddress(req.getAddress());
        userRepository.save(user);
        return toResponse(user);
    }

    private UserResponse toResponse(User u) {
        return UserResponse.builder()
                .id(u.getId())
                .keycloakId(u.getKeycloakId())
                .username(u.getUsername())
                .email(u.getEmail())
                .phone(u.getPhone())
                .address(u.getAddress())
                .build();
    }
}
