package com.example.July5.book.controller;

import com.example.July5.book.dto.AuthorRequest;
import com.example.July5.book.dto.AuthorResponse;
import com.example.July5.book.dto.BookRequest;
import com.example.July5.book.dto.BookResponse;
import com.example.July5.book.service.AuthorService;
import com.example.July5.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authors/v2")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;


    @GetMapping
    public List<AuthorResponse> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    @GetMapping("/{id}")
    public AuthorResponse getBookById(@PathVariable int id) {
        return authorService.getAuthorById(id);

    }

    @PostMapping
    public AuthorResponse addBook(@RequestBody AuthorRequest authorRequest) {
        return authorService.addAuthor(authorRequest);

    }


    @PutMapping("/{id}")
    public AuthorResponse updateBook(@PathVariable int id, @RequestBody AuthorRequest authorRequest) {
        return authorService.updateAuthor(id, authorRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteAuthor(@PathVariable int id) {

        authorService.deleteAuthor(id);
    }
}
