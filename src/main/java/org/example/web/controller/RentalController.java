package org.example.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.service.mapping.OverdueRentalMappingService;
import org.example.service.mapping.RentalMappingService;
import org.example.web.dto.rental.request.ClosedRentalFilterRequest;
import org.example.web.dto.rental.request.OverdueRentalFilterRequest;
import org.example.web.dto.rental.request.RentalCreateRequest;
import org.example.web.dto.rental.request.RentalProlongationRequest;
import org.example.web.dto.rental.response.OverdueRentalPageResponse;
import org.example.web.dto.rental.response.RentalPageResponse;
import org.example.web.dto.rental.response.RentalResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/v1/rentals")
@Tag(name = "Rental")
public class RentalController {

    private final RentalMappingService rentalMappingService;
    private final OverdueRentalMappingService overdueRentalMappingService;

    @GetMapping("/{id}")
    public ResponseEntity<RentalResponse> getRentalById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(rentalMappingService.getRentalById(id));
    }

    @PostMapping
    public ResponseEntity<RentalResponse> createRental(@Valid @RequestBody RentalCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rentalMappingService.createRental(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> closeRental(@PathVariable("id") Long id) {
        rentalMappingService.closeRental(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RentalResponse> prolongRental(@PathVariable("id") Long id, @Valid @RequestBody RentalProlongationRequest request) {
        return ResponseEntity.ok().body(rentalMappingService.updateRental(id, request));
    }

    @PostMapping("/overdue")
    public ResponseEntity<OverdueRentalPageResponse> getAllOverdueRentals(@Valid @RequestBody OverdueRentalFilterRequest request) {
        return ResponseEntity.ok().body(overdueRentalMappingService.getAllOverdueRentals(request));
    }

    @PostMapping("/returned")
    public ResponseEntity<RentalPageResponse> getAllClosedRentals(@Valid @RequestBody ClosedRentalFilterRequest request) {
        return ResponseEntity.ok().body(rentalMappingService.getAllClosedRentalsWithFilter(request));
    }
}
