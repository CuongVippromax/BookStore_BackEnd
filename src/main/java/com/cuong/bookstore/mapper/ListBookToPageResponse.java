package com.cuong.bookstore.mapper;

import com.cuong.bookstore.dto.response.BookResponse;
import com.cuong.bookstore.dto.response.PageResponse;
import com.cuong.bookstore.model.Book;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Setter
@Builder
@Service
public class ListBookToPageResponse {
    public static PageResponse getBookPageResponse(int page, int size, Page<Book> bookEntities){
        List<BookResponse> list = bookEntities.stream().map(book ->
                BookResponse.builder()
                        .title(book.getTitle())
                        .description(book.getDescription())
                        .category(book.getCategory())
                        .author(book.getAuthor())
                        .build()
                ).toList();
        PageResponse<List<BookResponse>> bookPageResponse = PageResponse.<List<BookResponse>>builder()
                .size(size)
                .page(page)
                .totalPage(bookEntities.getTotalPages())
                .totalElements((int) bookEntities.getTotalElements())
                .build();
        return bookPageResponse;
    }
}
