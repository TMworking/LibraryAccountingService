package org.example.mappers;

import org.example.domain.Book;
import org.example.model.Page;
import org.example.web.dto.book.request.BookCreateRequest;
import org.example.web.dto.book.request.BookUpdateRequest;
import org.example.web.dto.book.response.BookPageResponse;
import org.example.web.dto.book.response.BookResponse;
import org.example.web.dto.book.response.BookShortResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
@DecoratedWith(BookMapperDelegate.class)
public interface BookMapper {

    BookShortResponse toShortResponse(Book book);

    BookResponse toResponse(Book book);

    Book toBook(BookCreateRequest request);

    default BookPageResponse toPageResponse(Page<Book> bookPage) {
        return new BookPageResponse(
                bookPage.getContent().stream().map(this::toShortResponse).toList(),
                bookPage.getPageNumber(),
                bookPage.getPageSize(),
                bookPage.getTotalRecords()
        );
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromRequest(BookUpdateRequest request, @MappingTarget Book book);
}
