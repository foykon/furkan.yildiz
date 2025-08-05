package com.example.July5.book.controller;

import com.example.July5.book.dto.BookRequest;
import com.example.July5.book.dto.BookResponse;
import com.example.July5.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;


    @GetMapping
    public List<BookResponse> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public BookResponse getBookById(@PathVariable int id) {
        return bookService.getBookById(id);

    }

    @PostMapping
    public BookResponse addBook(@RequestBody BookRequest bookRequest) {
        return bookService.addBook(bookRequest);

    }


    @PutMapping("/{id}")
    public BookResponse updateBook(@PathVariable int id, @RequestBody BookRequest bookRequest) {
        return bookService.updateBook(id, bookRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable int id) {

        bookService.deleteBook(id);
    }


}
