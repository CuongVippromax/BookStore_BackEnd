package com.cuong.bookstore.config;

import com.cuong.bookstore.repository.RedisTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtDecoderCustom implements JwtDecoder {
    @Value("${jwt.secret-key}")
    private String secretKey;
    private final RedisTokenRepository redisTokenRepository;
    private  NimbusJwtDecoder nimbusJwtDecoder = null;
    
    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            // Check blacklist: Parse token để lấy JWT ID và kiểm tra trong Redis
            try {
                com.nimbusds.jwt.SignedJWT signedJWT = com.nimbusds.jwt.SignedJWT.parse(token);
                String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
                if (jwtId != null && redisTokenRepository.existsById(jwtId)) {
                    throw new JwtException("Token has been revoked (blacklisted)");
                }
            } catch (ParseException e) {
                // Nếu không parse được, để NimbusJwtDecoder xử lý
            }
            
            // NimbusJwtDecoder.decode() đã tự verify token rồi (signature, expiration)
            if(Objects.isNull(nimbusJwtDecoder)){
                SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HS256");
                nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                        .macAlgorithm(MacAlgorithm.HS256)
                        .build();
            }
            return nimbusJwtDecoder.decode(token);
        } catch (JwtException e) {
            throw e; // Giữ nguyên JwtException
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
