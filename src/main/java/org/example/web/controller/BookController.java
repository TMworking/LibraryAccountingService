package org.example.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.service.mapping.BookMappingService;
import org.example.web.dto.book.request.BookCreateRequest;
import org.example.web.dto.book.request.BookFilterRequest;
import org.example.web.dto.book.request.BookUpdateRequest;
import org.example.web.dto.book.response.BookPageResponse;
import org.example.web.dto.book.response.BookResponse;
import org.example.web.dto.rental.request.RentalFilterRequest;
import org.example.web.dto.rental.response.RentalPageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/books")
@Tag(name = "Book")
public class BookController {

    private final BookMappingService bookMappingService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN', 'USER')")
    public ResponseEntity<BookResponse> getBookById(@PathVariable("id") Long id) {
        BookResponse response = bookMappingService.getBookById(id);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/{id}/rentals")
    public ResponseEntity<RentalPageResponse> getBookRentals(@PathVariable("id") Long id, @Valid @RequestBody RentalFilterRequest request) {
        RentalPageResponse response = bookMappingService.getBookRentals(id, request);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN', 'USER')")
    public ResponseEntity<BookPageResponse> getAllBooks(@Valid @RequestBody BookFilterRequest request) {
        BookPageResponse response = bookMappingService.getAllBooksWithFilter(request);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public  ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookCreateRequest request) {
        BookResponse response = bookMappingService.createBook(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(@PathVariable("id") Long id, @Valid @RequestBody BookUpdateRequest request) {
        BookResponse response = bookMappingService.updateBook(id, request);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable("id") Long id) {
        bookMappingService.deleteBook(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
