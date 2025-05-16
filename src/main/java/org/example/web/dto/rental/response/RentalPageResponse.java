package org.example.web.dto.rental.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.web.dto.Meta;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalPageResponse {
    private List<RentalResponse> data;
    private Meta meta;
}
