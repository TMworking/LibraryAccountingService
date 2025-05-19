package org.example.web.dto.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequest {
    @NotBlank
    @Size(min = 1, max = 30)
    private String name;

    @NotBlank
    @Size(min = 1, max = 30)
    private String surname;

    @Size(max = 30)
    private String patronymic;

    @NotBlank
    @Pattern(regexp = "^\\+7[0-9]{10}$")
    private String phoneNumber;

    @NotBlank
    @Pattern(regexp = "^[0-9]{10}$")
    private String passport;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 4, max = 100)
    private String password;
}
