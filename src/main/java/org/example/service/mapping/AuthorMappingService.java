package org.example.service.mapping;

import org.example.web.dto.author.request.AuthorRequest;
import org.example.web.dto.author.request.AuthorSortRequest;
import org.example.web.dto.author.response.AuthorPageResponse;
import org.example.web.dto.author.response.AuthorResponse;
import org.example.web.dto.book.request.AddBooksRequest;

public interface AuthorMappingService {
    AuthorResponse getAuthorById(Long id);
    AuthorPageResponse getAuthorsWithSort(AuthorSortRequest request);
    AuthorResponse createAuthor(AuthorRequest request);
    AuthorResponse updateAuthor(Long id, AuthorRequest request);
    AuthorResponse addBooksToAuthor(Long id, AddBooksRequest request);
}
