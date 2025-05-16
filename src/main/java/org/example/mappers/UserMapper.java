package org.example.mappers;

import org.example.domain.User;
import org.example.model.Page;
import org.example.web.dto.Meta;
import org.example.web.dto.user.request.UserRegisterRequest;
import org.example.web.dto.user.request.UserUpdateRequest;
import org.example.web.dto.user.response.UserPageResponse;
import org.example.web.dto.user.response.UserRegisterResponse;
import org.example.web.dto.user.response.UserResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(User user);

    User toUser(UserRegisterRequest request);

    UserRegisterResponse toRegisterResponse(User user);

    default UserPageResponse toPageResponse(Page<User> page) {
        return new UserPageResponse(
                page.getContent().stream().map(this::toResponse).toList(),
                new Meta(page.getPageNumber(), page.getPageSize(), page.getTotalRecords())
        );
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromRequest(UserUpdateRequest request, @MappingTarget User user);
}
