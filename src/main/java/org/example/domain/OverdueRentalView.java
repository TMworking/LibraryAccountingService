package org.example.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import java.time.LocalDateTime;

@Entity
@Immutable
@Table(name = "overdue_rentals")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OverdueRentalView {

    @Id
    @Column(name = "rental_id")
    private Long rentalId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "book_id")
    private Long bookId;

    @Column(name = "rent_date")
    private LocalDateTime rentDate;

    @Column(name = "due_date")
    private LocalDateTime dueDate;
}
