package com.cuong.bookstore.service;

import com.cuong.bookstore.dto.request.ChangePasswordRequest;
import com.cuong.bookstore.dto.request.UserCreationRequest;
import com.cuong.bookstore.dto.response.UserResponse;
import com.cuong.bookstore.model.User;
import com.cuong.bookstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserCreationRequest userCreationRequest) {
        if(userRepository.existsByEmail(userCreationRequest.getEmail())) {
            throw new RuntimeException("User already exists!");
        }
        User newUser = User.builder()
                .username(userCreationRequest.getUsername())
                .email(userCreationRequest.getEmail())
                .password(passwordEncoder.encode(userCreationRequest.getPassword()))
                .address(userCreationRequest.getAddress())
                .phone(userCreationRequest.getPhone())
                .build();
        userRepository.save(newUser);
        return UserResponse.builder()
                .id(newUser.getId())
                .email(newUser.getEmail())
                .username(newUser.getUsername())
                .address(newUser.getAddress())
                .phone(newUser.getPhone())
                .build();
    }
    public UserResponse getUserByEmail(String email) {
        if(!userRepository.existsByEmail(email)) {
            throw new RuntimeException("User not found!");
        }
        User user = userRepository.findByEmail(email).orElse(null);
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .address(user.getAddress())
                .phone(user.getPhone())
                .build();
    }
    public String changePassword(ChangePasswordRequest  changePasswordRequest) {
        Optional<User> getUser = userRepository.findById(changePasswordRequest.getId());
        if(!getUser.isPresent()) {
            throw new RuntimeException("User not found!");
        }
        getUser.get().setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(getUser.get());
        return "Change Password Successfully!";
    }


}
