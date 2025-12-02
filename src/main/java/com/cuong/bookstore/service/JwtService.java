package com.cuong.bookstore.service;

import com.cuong.bookstore.common.TokenType;
import com.cuong.bookstore.dto.JwtInfo;
import com.cuong.bookstore.model.User;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtService {
    @Value("${jwt.secret-key}")
    private String secretKey;
    private final RedisService redisService;

    public String generateToken(User user, TokenType tokenType){
        //header
        JWSAlgorithm algorithm = tokenType == TokenType.ACCESS_TOKEN ? JWSAlgorithm.HS384 : JWSAlgorithm.HS512;
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        //payload
        Date issueTime = new Date();
        long expiration = tokenType == TokenType.ACCESS_TOKEN ? 5 : 7 ;
        Date expirationTime = Date.from(issueTime.toInstant().plus(expiration,tokenType == TokenType.ACCESS_TOKEN ? ChronoUnit.MINUTES : ChronoUnit.DAYS));
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .expirationTime(expirationTime)
                .issueTime(issueTime)
                .jwtID(UUID.randomUUID().toString())
                .build();
        Payload payload = new Payload(claimsSet.toJSONObject());
        //signature
        JWSObject  jwsObject = new JWSObject(header,payload);
        try {
            jwsObject.sign(new MACSigner(secretKey));
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
        return  jwsObject.serialize();
    }
    public boolean verifyToken(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        if(expirationTime.before(new Date())){
            return false;
        }
        return signedJWT.verify(new MACVerifier(secretKey));
    }

    public JwtInfo parseToken(String token) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
        Date issueTime = signedJWT.getJWTClaimsSet().getIssueTime();
        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        return JwtInfo.builder()
                .jwtID(jwtId)
                .issueTime(issueTime)
                .expiredTime(expirationTime)
                .build();
    }
}
