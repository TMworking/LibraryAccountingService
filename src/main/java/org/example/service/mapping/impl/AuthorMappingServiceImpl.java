package org.example.service.mapping.impl;

import lombok.RequiredArgsConstructor;
import org.example.mappers.AuthorMapper;
import org.example.service.domain.AuthorService;
import org.example.service.mapping.AuthorMappingService;
import org.example.web.dto.author.request.AuthorRequest;
import org.example.web.dto.author.request.AuthorSortRequest;
import org.example.web.dto.author.response.AuthorPageResponse;
import org.example.web.dto.author.response.AuthorResponse;
import org.example.web.dto.book.request.AddBooksRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthorMappingServiceImpl implements AuthorMappingService {

    private final AuthorService authorService;
    private final AuthorMapper authorMapper;

    @Override
    public AuthorResponse getAuthorById(Long id) {
        return authorMapper.toResponse(authorService.findById(id));
    }

    @Override
    public AuthorPageResponse getAuthorsWithSort(AuthorSortRequest request) {
        return authorMapper.toPageResponse(authorService.findPageAuthorsWithSort(request));
    }

    @Override
    public AuthorResponse createAuthor(AuthorRequest request) {
        return authorMapper.toResponse(authorService.saveAuthor(authorMapper.toAuthor(request)));
    }

    @Override
    public AuthorResponse updateAuthor(Long id, AuthorRequest request) {
        return authorMapper.toResponse(authorService.updateAuthor(authorMapper.toAuthor(id, request)));
    }

    @Override
    public AuthorResponse addBooksToAuthor(Long id, AddBooksRequest request) {
        return authorMapper.toResponse(authorService.addBooksToAuthor(id, request.getBooksIds()));
    }
}
