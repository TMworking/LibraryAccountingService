package org.example.web.dto.rental.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalCreateRequest {

    @NotNull
    private Long userId;

    @NotNull
    private Long bookId;

    @NotNull
    private LocalDateTime rentDate;

    @NotNull
    private Integer duration;
}
