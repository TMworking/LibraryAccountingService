package org.example.service.mapping;

import org.example.web.dto.book.request.BookCreateRequest;
import org.example.web.dto.book.request.BookFilterRequest;
import org.example.web.dto.book.request.BookUpdateRequest;
import org.example.web.dto.book.response.BookPageResponse;
import org.example.web.dto.book.response.BookResponse;
import org.example.web.dto.rental.request.RentalFilterRequest;
import org.example.web.dto.rental.response.RentalPageResponse;

public interface BookMappingService {
    BookResponse getBookById(Long id);
    RentalPageResponse getBookRentals(Long bookId, RentalFilterRequest request);
    BookPageResponse getAllBooksWithFilter(BookFilterRequest request);
    BookResponse createBook(BookCreateRequest request);
    BookResponse updateBook(Long id, BookUpdateRequest request);
    void deleteBook(Long id);
}
