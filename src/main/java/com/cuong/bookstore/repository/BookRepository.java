package com.cuong.bookstore.repository;

import com.cuong.bookstore.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.Optional;

public interface BookRepository extends JpaRepository<Book,Long> {
    Optional<Book> getBookById(int id);
    Optional<Book> getBookByTitle(String title);
    void deleteById(int id);
    @Query(value = "select b from Book b where (lower(b.title)) like :keyword "
            + "or (lower(b.author)) like :keyword "
            + "or (lower(b.category)) like :keyword")
    Page<Book> searchByKeyword(String keyword, Pageable pageable);
}
