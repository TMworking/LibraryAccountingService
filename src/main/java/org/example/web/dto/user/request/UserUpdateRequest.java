package org.example.web.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    @NotBlank
    @Size(min = 1, max = 30)
    private String name;

    @NotBlank
    @Size(min = 1, max = 30)
    private String surname;

    @Size(max = 30)
    private String patronymic;

    @NotBlank
    @Pattern(regexp = "^\\+?[0-9\\-\\s()]{7,20}$")
    private String phoneNumber;
}
