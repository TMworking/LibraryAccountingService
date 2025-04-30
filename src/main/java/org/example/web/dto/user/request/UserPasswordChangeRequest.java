package org.example.web.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPasswordChangeRequest {

    @NotBlank
    private String oldPassword;

    @NotBlank
    @Size(min = 4, max = 100)
    private String newPassword;
}
