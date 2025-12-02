package com.cuong.bookstore.service;

import com.cuong.bookstore.dto.request.BookRequest;
import com.cuong.bookstore.dto.response.BookResponse;
import com.cuong.bookstore.dto.response.PageResponse;
import com.cuong.bookstore.model.Book;
import com.cuong.bookstore.model.User;
import com.cuong.bookstore.repository.BookRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {
    private final BookRepository bookRepository;
    public BookResponse addBook(BookRequest bookRequest) {
        if(bookRepository.getBookByTitle(bookRequest.getTitle()) == null){
            Book newBook = Book.builder()
                    .title(bookRequest.getTitle())
                    .description(bookRequest.getDescription())
                    .author(bookRequest.getAuthor())
                    .category(bookRequest.getCategory())
                    .build();
            bookRepository.save(newBook);
            return BookResponse.builder()
                    .title(bookRequest.getTitle())
                    .author(bookRequest.getAuthor())
                    .description(bookRequest.getDescription())
                    .category(bookRequest.getCategory())
                    .build();
        }else{
            return null;
        }
    }
    public BookResponse getBookById(int id){
        Book getBook = bookRepository.getBookById(id).orElse(null);
        return BookResponse.builder()
                .title(getBook.getTitle())
                .author(getBook.getAuthor())
                .description(getBook.getDescription())
                .category(getBook.getCategory())
                .build();
    }
    public BookResponse  updateBook(int id , BookRequest bookRequest) {
        Book getBook = bookRepository.getBookById(id).orElse(null);
        if(getBook != null){
            if(bookRequest.getTitle() != null){
                getBook.setTitle(bookRequest.getTitle());
            }
            if(bookRequest.getAuthor() != null){
                getBook.setAuthor(bookRequest.getAuthor());
            }
            if(bookRequest.getDescription() != null){
                getBook.setDescription(bookRequest.getDescription());
            }
            if(bookRequest.getCategory() != null){
                getBook.setCategory(bookRequest.getCategory());
            }
            bookRepository.save(getBook);
        }else{
            throw new RuntimeException("Khong tim thay sach !!!");
        }
        return BookResponse.builder()
                .title(bookRequest.getTitle())
                .author(bookRequest.getAuthor())
                .description(bookRequest.getDescription())
                .category(bookRequest.getCategory())
                .build();
    }
    public void deleteBookById(int id) {
        bookRepository.deleteById(id);
    }
    public PageResponse<BookResponse> findAllBook(int page, int size , String keyword , String sort){
        //checck page bắt đầu từ trang 1
        int oageNo = page > 0 ? page - 1 : 0;

        //Sorting
        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "id");
        if (StringUtils.hasLength(sort)) {
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sort);
            if(matcher.find()){
                String columnName = matcher.group(1);
                if(matcher.group(3).equalsIgnoreCase("asc")){
                    order = new Sort.Order(Sort.Direction.ASC, columnName);
                }else{
                    order = new Sort.Order(Sort.Direction.DESC, columnName);
                }
            }
        }
        //Paging
        Pageable pageable = PageRequest.of(page, size, Sort.by(order));
        Page<Book> entityPage ;
        if(StringUtils.hasLength(keyword)){
            keyword = "%"+keyword+"%";
            entityPage = bookRepository.searchByKeyword(keyword, pageable);
        }else{
            entityPage = bookRepository.findAll(pageable);
        }
        return
    }
}
