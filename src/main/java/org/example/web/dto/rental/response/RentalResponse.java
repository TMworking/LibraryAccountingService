package org.example.web.dto.rental.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalResponse {
    private Long id;
    private Long userId;
    private Long bookId;
    private LocalDateTime rentDate;
    private Integer duration;
}
