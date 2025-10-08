package com.cuong.bookstore.service;

import com.cuong.bookstore.common.TokenType;
import com.cuong.bookstore.model.User;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.lettuce.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class JwtService {
    @Value("${jwt.secret-key}")
    private String secretKey;

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
    public boolean verifyToken(String token){
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            if (signedJWT.getJWTClaimsSet().getExpirationTime().before(new Date())) {
                return false;
            }
            return true;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
