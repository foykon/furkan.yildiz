package com.example.July5.book.service.impl;

import com.example.July5.book.dto.BookRequest;
import com.example.July5.book.dto.BookResponse;
import com.example.July5.book.entity.Book;
import com.example.July5.book.repository.BookRepository;
import com.example.July5.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImplWithJpa implements BookService {
    private final BookRepository bookRepository;

    @Override
    public BookResponse addBook(BookRequest bookRequest) {
        Book book = mapRequestToBook(bookRequest);
        bookRepository.save(book);
        return mapBookToResponse(book);
    }

    @Override
    public BookResponse getBookById(int id) {
        Book book = bookRepository.findById(id).get();

        return mapBookToResponse(book);
    }

    @Override
    public BookResponse updateBook(int id, BookRequest bookRequest) {
        Book book = bookRepository.findById(id).get();
        book.setTitle(bookRequest.getTitle());
        book.setAuthor(bookRequest.getAuthor());
        book.setPages(bookRequest.getPages());
        bookRepository.save(book);

        return mapBookToResponse(book);
    }

    @Override
    public void deleteBook(int id) {
        bookRepository.deleteById(id);

    }

    @Override
    public List<BookResponse> getAllBooks() {
        return bookRepository.findAll().stream().map(this::mapBookToResponse).toList();
    }

    private BookResponse mapBookToResponse(Book book){
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .pages(book.getPages())
                .build();
    }

    private Book mapRequestToBook(BookRequest bookRequest){
        return Book.builder()
                .title(bookRequest.getTitle())
                .author(bookRequest.getAuthor())
                .pages(bookRequest.getPages())
                .build();
    }
}
