package com.cuong.bookstore.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {
    private int page;
    private int size;
    private int totalElements;
    private int totalPage;
    private T data;
}
