package org.example.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.mappers.AuthorMapper;
import org.example.service.AuthorService;
import org.example.web.dto.author.request.AuthorRequest;
import org.example.web.dto.book.request.AddBooksRequest;
import org.example.web.dto.author.request.AuthorSortRequest;
import org.example.web.dto.author.response.AuthorPageResponse;
import org.example.web.dto.author.response.AuthorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/authors")
@Tag(name = "Author")
// TODO: for ADMIN and LIBRARIAN
public class AuthorController {

    private final AuthorService authorService;
    private final AuthorMapper authorMapper;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN', 'USER')")
    public ResponseEntity<AuthorResponse> getAuthorById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(authorMapper.toResponse(authorService.findById(id)));
    }

    @PostMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN', 'USER')")
    public ResponseEntity<AuthorPageResponse> getAllAuthors(@Valid @RequestBody AuthorSortRequest request) {
        return ResponseEntity.ok().body(authorMapper.toPageResponse(authorService.findPageAuthorsWithSort(request)));
    }

    @PostMapping
    public ResponseEntity<AuthorResponse> createAuthor(@Valid @RequestBody AuthorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                authorMapper.toResponse(authorService.saveAuthor(authorMapper.toAuthor(request)))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorResponse> updateAuthor(@PathVariable("id") Long id, AuthorRequest request) {
        return ResponseEntity.ok().body(
                authorMapper.toResponse(authorService.saveAuthor(authorMapper.toAuthor(id, request)))
        );
    }

    @PatchMapping("/{id}/books")
    public ResponseEntity<AuthorResponse> addBooksToAuthor(@PathVariable("id") Long id, @Valid @RequestBody AddBooksRequest request) {
        return ResponseEntity.ok().body(
                authorMapper.toResponse(authorService.addBooksToAuthor(id, request.getBooksIds()))
        );
    }
}
