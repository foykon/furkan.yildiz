package com.example.July5.book.service;

import com.example.July5.book.dto.AuthorRequest;
import com.example.July5.book.dto.AuthorResponse;
import com.example.July5.book.dto.AuthorSerachRequest;

import java.util.List;

public interface AuthorService {
    AuthorResponse addAuthor(AuthorRequest authorRequest);
    AuthorResponse updateAuthor(int id, AuthorRequest authorRequest);
    void deleteAuthor(int id);
    List<AuthorResponse> getAllAuthors();
    AuthorResponse getAuthorById(int id);

    List<AuthorResponse> searchAuthors(AuthorSerachRequest authorSerachRequest);
}
