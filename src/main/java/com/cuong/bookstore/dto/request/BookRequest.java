package com.cuong.bookstore.dto.request;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookRequest {
    private String title;
    private String author;
    private String description;
    private String category;
    private Date createdAt;
    private Date updatedAt;
}
