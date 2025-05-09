package org.example.repository;

import org.example.domain.Author;
import org.example.model.Page;
import org.example.web.dto.author.request.AuthorSortRequest;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {
    Optional<Author> findById(Long id);
    List<Author> findAllByIds(List<Long> ids);
    Page<Author> findAll(AuthorSortRequest filterRequest);
    Author save(Author author);
    Author update(Author author);
}
