package com.cuong.electronicstore.dto.response;

import lombok.*;

import java.util.Date;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private int code;
    private String message;
    private String path;
    private Date timestamp;
    private Map<String, String> errors;
}
