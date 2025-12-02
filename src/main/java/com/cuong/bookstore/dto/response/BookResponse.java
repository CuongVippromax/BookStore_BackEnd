package com.cuong.bookstore.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookResponse {
    private String title;
    private String author;
    private String description;
    private String category;
}
