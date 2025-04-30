package org.example.web.dto.book.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookPageResponse {
    private List<BookShortResponse> responses;
    private int pageNumber;
    private int pageSize;
    private long totalRecords;
}
