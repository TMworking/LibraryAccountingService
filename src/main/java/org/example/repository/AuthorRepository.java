package org.example.repository;

import org.example.model.domain.Author;
import org.example.web.dto.author.request.AuthorSortRequest;

import java.util.Optional;

public interface AuthorRepository {
    Optional<Author> findById(Long id);
    Page<Author> findAll(AuthorSortRequest filterRequest);
    Author save(Author author);
    Author update(Author author);
}
