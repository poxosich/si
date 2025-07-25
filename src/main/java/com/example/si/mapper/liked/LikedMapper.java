package com.example.si.mapper.liked;

import com.example.si.dto.liked.LikedResponse;
import com.example.si.entity.Liked;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LikedMapper {
    LikedResponse toDto(Liked liked);
}
