package com.example.July5.book.service;

import com.example.July5.book.dto.BookRequest;
import com.example.July5.book.dto.BookResponse;
import com.example.July5.book.dto.request.BookSearchRequest;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface BookService {

    BookResponse addBook(@Valid BookRequest bookRequest);
    BookResponse getBookById(int id);
    BookResponse updateBook(int id, @Valid BookRequest bookRequest);
    void deleteBook(int id);
    List<BookResponse> getAllBooks();

}
