package com.vandev.manage.mapper;


import com.vandev.manage.dto.UserSystemDTO;
import com.vandev.manage.pojo.UserSystem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserSystemDTO toDTO(UserSystem user);

    UserSystem toEntity(UserSystemDTO userDTO);
}