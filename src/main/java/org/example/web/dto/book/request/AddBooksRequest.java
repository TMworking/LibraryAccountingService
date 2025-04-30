package org.example.web.dto.book.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddBooksRequest {

    @NotEmpty
    private List<Long> booksIds;
}
