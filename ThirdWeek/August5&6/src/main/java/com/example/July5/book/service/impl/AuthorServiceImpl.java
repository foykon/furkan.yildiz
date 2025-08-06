package com.example.July5.book.service.impl;

import com.example.July5.book.dto.AuthorRequest;
import com.example.July5.book.dto.AuthorResponse;
import com.example.July5.book.dto.BookRequest;
import com.example.July5.book.dto.BookResponse;
import com.example.July5.book.entity.Author;
import com.example.July5.book.entity.Book;
import com.example.July5.book.repository.AuthorRepository;
import com.example.July5.book.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Override
    public AuthorResponse addAuthor(AuthorRequest authorRequest) {
        Author author = mapRequestToAuthor(authorRequest);
        authorRepository.save(author);
        return mapAuthorToResponse(author);
    }

    @Override
    public AuthorResponse getAuthorById(int id) {
        Author author = authorRepository.findById(id).get();

        return mapAuthorToResponse(author);
    }

    @Override
    public AuthorResponse updateAuthor(int id, AuthorRequest bookRequest) {
        Author author = authorRepository.findById(id).get();
        author.setName(bookRequest.getName());
        author.setSurname(bookRequest.getSurname());
        authorRepository.save(author);

        return mapAuthorToResponse(author);
    }

    @Override
    public void deleteAuthor(int id) {
        authorRepository.deleteById(id);

    }

    @Override
    public List<AuthorResponse> getAllAuthors() {
        return authorRepository.findAll().stream().map(this::mapAuthorToResponse).toList();
    }

    private AuthorResponse mapAuthorToResponse(Author author){
        return AuthorResponse.builder()
                .id(author.getId())
                .createdAt(author.getCreatedAt())
                .updatedAt(author.getUpdatedAt())
                .name(author.getName())
                .surname(author.getSurname())
                .build();
    }

    private Author mapRequestToAuthor(AuthorRequest authorRequest){
        return Author.builder()
                .name(authorRequest.getName())
                .surname(authorRequest.getSurname())
                .build();
    }
}
