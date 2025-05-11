package org.example.web.dto.user.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.web.dto.role.RoleResponse;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String name;
    private String surname;
    private String patronymic;
    private String email;
    private String phoneNumber;
    private String passport;
    private List<RoleResponse> roles;
}
