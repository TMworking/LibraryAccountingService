package org.example.web.dto.rental.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalResponse {
    private Long id;
    private Long userId;
    private Long bookId;
    private LocalDate rentDate;
    private Integer duration;
}
