package org.example.web.dto.rental.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OverdueRentalResponse {
    private Long id;
    private String userName;
    private String userSurname;
    private String userEmail;
    private String userPhone;
    private LocalDate rentDate;
    private LocalDate dueDate;
}
