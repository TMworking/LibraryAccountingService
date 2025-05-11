package org.example.mappers;

import org.example.domain.Role;
import org.example.web.dto.role.RoleResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleResponse toResponse(Role role);
}
