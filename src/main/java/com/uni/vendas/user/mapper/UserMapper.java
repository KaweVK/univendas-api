package com.uni.vendas.user.mapper;

import com.uni.vendas.user.dto.DefaultUserDTO;
import com.uni.vendas.user.dto.RegisterUserDTO;
import com.uni.vendas.user.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "image", ignore = true)
    User toEntity(RegisterUserDTO userDTO);

    @Mapping(target = "image", ignore = true)
    RegisterUserDTO toRegisterDTO(User user);

    DefaultUserDTO toDefaultDTO(User user);
}
