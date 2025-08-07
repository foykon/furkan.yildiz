package com.example.July5.book.service.impl;

import com.example.July5.book.dto.BookRequest;
import com.example.July5.book.dto.BookResponse;
import com.example.July5.book.entity.Book;
import com.example.July5.book.exception.BookNotFoundException;
import com.example.July5.book.service.BookService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    private List<Book> books;

    public BookServiceImpl() {
        this.books = new ArrayList<>();
    }


    @Override
    public BookResponse addBook(BookRequest bookRequest) {
        Book bookAdded = mapRequestToBook(bookRequest);
        bookAdded.setId(books.size() + 1);
        books.add(bookAdded);
        return mapBookToResponse(bookAdded);

    }

    @Override
    public BookResponse getBookById(int id) {
        Optional<Book> bookOptional = books.stream().filter(book -> book.getId() == id).findFirst();
        if(bookOptional.isEmpty()){
            throw new BookNotFoundException("Book not found");
        }
        return mapBookToResponse(bookOptional.get());
    }

    @Override
    public BookResponse updateBook(int id, BookRequest bookRequest) {
        Book updatedBook = books.get(id);
        if(updatedBook == null){
            throw new BookNotFoundException("Book not found");
        }
        updatedBook.setTitle(bookRequest.getTitle());
        //updatedBook.setAuthor(bookRequest.getAuthor());
        updatedBook.setPages(bookRequest.getPages());
        return mapBookToResponse(updatedBook);
    }

    @Override
    public void deleteBook(int id) {
        books.remove(id);
    }

    @Override
    public List<BookResponse> getAllBooks() {
        return books.stream().map(this::mapBookToResponse).collect(Collectors.toList());
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
                //.author(bookRequest.getAuthor())
                .pages(bookRequest.getPages())
                .build();
    }
}
