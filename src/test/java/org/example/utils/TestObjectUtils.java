package org.example.utils;

import org.example.domain.Author;
import org.example.domain.Book;
import org.example.domain.Catalog;
import org.example.domain.OverdueRentalView;
import org.example.domain.RefreshToken;
import org.example.domain.Rental;
import org.example.domain.Role;
import org.example.domain.User;
import org.example.enums.UserRole;

import java.time.LocalDate;

public class TestObjectUtils {

    public static Book createTestBook() {
        Book book = new Book();
        book.setId(1L);
        book.setAvailableCount(1);
        book.setName("Spring in Action");
        return book;
    }

    public static Author createTestAuthor() {
        Author author = new Author();
        author.setId(1L);
        author.setName("Leeroy");
        author.setSurname("Jenkins");
        return author;
    }

    public static Catalog createTestCatalog() {
        Catalog catalog = new Catalog();
        catalog.setId(1L);
        catalog.setName("Test");
        return catalog;
    }

    public static OverdueRentalView createTestOverdueRentalView() {
        OverdueRentalView overdueRentalView = new OverdueRentalView();
        overdueRentalView.setRentalId(1L);
        overdueRentalView.setBookId(1L);
        overdueRentalView.setUserId(1L);
        return overdueRentalView;
    }

    public static User createTestUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setName("Vasya");
        user.setSurname("Pupkin");
        user.setPassword("password");
        return user;
    }

    public static Rental createTestRental() {
        Rental rental = new Rental();
        rental.setId(1L);
        return rental;
    }

    public static Rental createTestRental(User user, Book book, LocalDate rentDate, Integer duration) {
        Rental rental = new Rental();
        rental.setId(1L);
        rental.setBook(book);
        rental.setUser(user);
        rental.setRentDate(rentDate);
        rental.setDuration(duration);
        return rental;
    }

    public static Role createTestRole(UserRole userRole) {
        Role role = new Role();
        role.setId(1L);
        role.setUserRole(userRole);
        return role;
    }

    public static RefreshToken createTestRefreshToken() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(1L);
        refreshToken.setToken("refresh-token");
        return refreshToken;
    }
}
