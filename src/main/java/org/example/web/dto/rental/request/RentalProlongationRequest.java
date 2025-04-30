package org.example.web.dto.rental.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalProlongationRequest {

    @NotNull
    @Min(value = 1)
    private int days;
}
