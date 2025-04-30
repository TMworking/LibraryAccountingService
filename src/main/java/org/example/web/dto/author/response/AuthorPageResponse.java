package org.example.web.dto.author.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorPageResponse {
    private List<AuthorResponse> authorResponses;
    private int pageNumber;
    private int pageSize;
    private long totalRecords;
}
