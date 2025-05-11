package org.example.service.mapping.impl;

import lombok.RequiredArgsConstructor;
import org.example.domain.Book;
import org.example.domain.Rental;
import org.example.mappers.BookMapper;
import org.example.mappers.RentalMapper;
import org.example.model.Page;
import org.example.service.domain.BookService;
import org.example.service.domain.CatalogService;
import org.example.service.mapping.BookMappingService;
import org.example.web.dto.book.request.BookCreateRequest;
import org.example.web.dto.book.request.BookFilterRequest;
import org.example.web.dto.book.request.BookUpdateRequest;
import org.example.web.dto.book.response.BookPageResponse;
import org.example.web.dto.book.response.BookResponse;
import org.example.web.dto.rental.request.RentalFilterRequest;
import org.example.web.dto.rental.response.RentalPageResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookMappingServiceImpl implements BookMappingService {

    private final BookService bookService;
    private final BookMapper bookMapper;
    private final RentalMapper rentalMapper;
    private final CatalogService catalogService;

    @Override
    public BookResponse getBookById(Long id) {
        return bookMapper.toResponse(bookService.findById(id));
    }

    @Override
    public RentalPageResponse getBookRentals(Long bookId, RentalFilterRequest request) {
        Page<Rental> rentalPage = bookService.getBookRentals(bookId, request);
        return rentalMapper.toPageResponse(rentalPage);
    }

    @Override
    public BookPageResponse getAllBooksWithFilter(BookFilterRequest request) {
        if (request.getCatalogIds() != null && !request.getCatalogIds().isEmpty()) {
            List<Long> allCatalogIds = catalogService.findAllSubCatalogIds(request.getCatalogIds());
            request.setCatalogIds(allCatalogIds);
        }
        Page<Book> bookPage = bookService.findBooksWithFilter(request);
        return bookMapper.toPageResponse(bookPage);
    }

    @Override
    public BookResponse createBook(BookCreateRequest request) {
        Book book = bookMapper.toBook(request);
        Book savedBook = bookService.createBook(book);
        return bookMapper.toResponse(savedBook);
    }

    @Override
    public BookResponse updateBook(Long id, BookUpdateRequest request) {
        Book existingBook = bookService.findById(id);
        bookMapper.updateFromRequest(request, existingBook);
        Book updatedBook = bookService.updateBook(existingBook);
        return bookMapper.toResponse(updatedBook);
    }

    @Override
    public void deleteBook(Long id) {
        bookService.deleteBook(id);
    }
}
