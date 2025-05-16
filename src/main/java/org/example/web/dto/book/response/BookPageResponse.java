package org.example.web.dto.book.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.web.dto.Meta;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookPageResponse {
    private List<BookShortResponse> data;
    private Meta meta;
}
