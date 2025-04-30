package org.example.web.dto.rental.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalPageResponse {
    private List<RentalResponse> rentalResponses;
    private int pageNumber;
    private int pageSize;
    private long totalRecords;
}
