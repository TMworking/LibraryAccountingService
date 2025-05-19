package org.example.web.dto.user.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    @Size(min = 1, max = 30)
    private String name;

    @Size(min = 1, max = 30)
    private String surname;

    @Size(max = 30)
    private String patronymic;

    @Pattern(regexp = "^\\+7[0-9]{10}$")
    private String phoneNumber;
}
