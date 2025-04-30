package org.example.mappers;

import org.example.model.domain.Author;
import org.example.repository.Page;
import org.example.web.dto.author.request.AuthorRequest;
import org.example.web.dto.author.response.AuthorPageResponse;
import org.example.web.dto.author.response.AuthorResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    AuthorResponse toResponse(Author author);

    Author toAuthor(AuthorRequest request);

    default Author toAuthor(Long id, AuthorRequest request) {
        Author author = toAuthor(request);
        author.setId(id);
        return author;
    }


    default AuthorPageResponse toPageResponse(Page<Author> page) {
        return new AuthorPageResponse(
                page.getContent().stream().map(this::toResponse).toList(),
                page.getPageNumber(),
                page.getPageSize(),
                page.getTotalRecords()
        );
    }
}
