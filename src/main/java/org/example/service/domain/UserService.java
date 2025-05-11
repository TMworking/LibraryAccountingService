package org.example.service.domain;

import org.example.domain.Rental;
import org.example.domain.User;
import org.example.model.Page;
import org.example.web.dto.rental.request.RentalFilterRequest;

public interface UserService {
    User findById(Long id);
    Page<Rental> getUserRentals(Long id, RentalFilterRequest filterRequest);
    User create(User user);
    User update(User user);
    void changeUserPassword(User user, String oldPassword, String newPassword);
}
