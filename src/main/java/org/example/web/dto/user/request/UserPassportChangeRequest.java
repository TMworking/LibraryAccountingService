package org.example.web.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPassportChangeRequest {

    @NotBlank
    @Pattern(regexp = "^[0-9]{10}$")
    private String passport;
}
