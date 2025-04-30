package org.example.web.dto.user.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPageResponse {
    private List<UserResponse> rentalResponses;
    private int pageNumber;
    private int pageSize;
    private long totalRecords;
}
