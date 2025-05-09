package org.example.service.domain;

import org.example.domain.Author;
import org.example.model.Page;
import org.example.web.dto.author.request.AuthorSortRequest;

import java.util.List;

public interface AuthorService {
    Author findById(Long id);
    Page<Author> findPageAuthorsWithSort(AuthorSortRequest request);
    Author saveAuthor(Author author);
    Author updateAuthor(Author author);
    Author addBooksToAuthor(Long id, List<Long> bookIds);
    List<Author> getAllByIds(List<Long> ids);
}
