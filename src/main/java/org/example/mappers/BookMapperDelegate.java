package org.example.mappers;

import org.example.domain.Author;
import org.example.domain.Book;
import org.example.domain.Catalog;
import org.example.service.domain.AuthorService;
import org.example.service.domain.CatalogService;
import org.example.web.dto.book.request.BookCreateRequest;
import org.example.web.dto.book.request.BookUpdateRequest;
import org.example.web.dto.book.response.BookResponse;
import org.example.web.dto.book.response.BookShortResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class BookMapperDelegate implements BookMapper {

    @Autowired
    private BookMapper delegate;
    @Autowired
    private AuthorService authorService;
    @Autowired
    private CatalogService catalogService;

    @Override
    public BookShortResponse toShortResponse(Book book) {
        return delegate.toShortResponse(book);
    }

    @Override
    public BookResponse toResponse(Book book) {
        return delegate.toResponse(book);
    }

    @Override
    public Book toBook(BookCreateRequest request) {
        Book book = delegate.toBook(request);

        List<Author> authors = authorService.getAllByIds(request.getAuthorIds());
        List<Catalog> catalogs = catalogService.getAllByIds(request.getCatalogIds());
        book.addAuthors(authors);
        book.addCatalogs(catalogs);

        return book;
    }

    @Override
    public void updateEntityFromDto(BookUpdateRequest request, Book book) {
        delegate.updateEntityFromDto(request, book);
    }
}
