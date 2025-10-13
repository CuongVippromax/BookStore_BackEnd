package com.cuong.bookstore.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class JwtInfo implements Serializable {
    private String jwtID;
    private Date issueTime;
    private Date expiredTime;
}
