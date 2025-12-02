package com.cuong.bookstore.service;

import com.cuong.bookstore.dto.request.ChangePasswordRequest;
import com.cuong.bookstore.dto.request.UserCreationRequest;
import com.cuong.bookstore.dto.response.PageResponse;
import com.cuong.bookstore.dto.response.UserResponse;
import com.cuong.bookstore.model.User;
import com.cuong.bookstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.cuong.bookstore.mapper.ListUserToPageResponse.getUserPageResponse;

@Service
@RequiredArgsConstructor
@Slf4j
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

    public PageResponse findAllUsers(String keyword,String sort , int page, int size) {

        //chỉnh sử cho phù hợp với UX
       int pageNo = page > 0 ? page-1 : 0 ;

       //Sorting
        Sort.Order order = new Sort.Order(Sort.Direction.ASC,"id");
        if(StringUtils.hasLength(sort)){
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sort);
            if(matcher.find()){
                String columnName = matcher.group(1);
                if(matcher.group(3).equalsIgnoreCase("asc")){
                    order = new Sort.Order(Sort.Direction.ASC,columnName);
                }else{
                    order = new Sort.Order(Sort.Direction.DESC,columnName);
                }
            }
        }

        //Paging
        Pageable pageable = PageRequest.of(page,size,Sort.by(order));
        Page<User> entityPage ;
        if(StringUtils.hasLength(keyword)){
            keyword = "%"+keyword+"%";
            entityPage = userRepository.searchByKeyword(keyword, pageable);
        }else{
            entityPage = userRepository.findAll(pageable);
        }
        return getUserPageResponse(page,size,entityPage);
    }


}
