package com.example.si.mapper.user;

import com.example.si.dto.user.UserDtoRequest;
import com.example.si.dto.user.UserDtoResponse;
import com.example.si.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserDtoRequest userDtoRequest);

    UserDtoResponse toDto(User user);

    UserDtoRequest toRequestDto(UserDtoResponse userDtoResponse);

}
