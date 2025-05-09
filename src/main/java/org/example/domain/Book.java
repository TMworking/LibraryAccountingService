package org.example.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "available_count")
    private Integer availableCount;

    @Column(name = "publishing_date")
    private LocalDate publishingDate;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "books")
    private List<Catalog> catalogs = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "books")
    private List<Author> authors = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "book", cascade = CascadeType.MERGE)
    private List<Rental> rentals = new ArrayList<>();

    public void addRental(Rental rental) {
        this.rentals.add(rental);
        rental.setBook(this);
    }

    public void addAuthors(List<Author> authors) {
        for (Author author : authors) {
            this.authors.add(author);
            author.getBooks().add(this);
        }
    }

    public void addCatalogs(List<Catalog> catalogs) {
        for (Catalog catalog : catalogs) {
            this.catalogs.add(catalog);
            catalog.getBooks().add(this);
        }
    }
}
