package org.example.web.dto.role;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enums.UserRole;
import org.example.enums.RoleAction;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleRequest {
    @NotNull
    private RoleAction roleAction;
    @NotNull
    private UserRole userRole;
}
