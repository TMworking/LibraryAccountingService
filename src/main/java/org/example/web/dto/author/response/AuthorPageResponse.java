package org.example.web.dto.author.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.web.dto.Meta;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorPageResponse {
    private List<AuthorResponse> data;
    private Meta meta;
}
