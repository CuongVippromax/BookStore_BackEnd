package com.cuong.bookstore.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "book")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Book extends AbstractEntity<Long> {
    private String title;
    private String author;
    private String description;
    private String category;
}
