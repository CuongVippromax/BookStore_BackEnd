package com.cuong.bookstore.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@RedisHash("RedisHash")
public class RedisToken {
    @Id
    private String jwtId;
    @TimeToLive(unit = TimeUnit.MINUTES)
    private long expirationTime;
}
