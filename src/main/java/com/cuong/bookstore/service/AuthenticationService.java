package com.cuong.bookstore.service;

import com.cuong.bookstore.common.TokenType;
import com.cuong.bookstore.dto.JwtInfo;
import com.cuong.bookstore.dto.request.LoginRequest;
import com.cuong.bookstore.dto.response.LoginResponse;
import com.cuong.bookstore.model.RedisToken;
import com.cuong.bookstore.model.User;
import com.cuong.bookstore.repository.RedisTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final  AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RedisService redisService;
    private final RedisTokenRepository redisTokenRepository;

    public LoginResponse login(LoginRequest loginRequest){
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
        Authentication authenticate = authenticationManager.authenticate(token);

        User user = (User) authenticate.getPrincipal();

        return LoginResponse.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .accessToken(jwtService.generateToken(user, TokenType.ACCESS_TOKEN))
                .refreshToken(jwtService.generateToken(user, TokenType.REFRESH_TOKEN))
                .build();
    }
    public void logout(String token) throws ParseException {
        JwtInfo jwtInfo = jwtService.parseToken(token);


        //Lưu JWT ID chứ không phải lưu cả token vào redis
        String jwtId = jwtInfo.getJwtID();
        Date issueTime = jwtInfo.getIssueTime();
        Date expirateTime = jwtInfo.getExpiredTime();

        if(expirateTime.before(new Date())){
            return ;
        }

        RedisToken redisToken = RedisToken.builder()
                .jwtId(jwtId)
                .expirationTime(expirateTime.getTime()-(new Date().getTime()))
                .build();
        redisTokenRepository.save(redisToken);
        log.info("Logout successful!");
    }
}
