package com.caito.ecommerce.usersevice.utils.mappers;


import com.caito.ecommerce.usersevice.api.models.requests.UserRequest;
import com.caito.ecommerce.usersevice.api.models.responses.UserResponse;
import com.caito.ecommerce.usersevice.persistence.entities.UserEntity;

public class UserMapper {

    public static UserEntity mapToEntity(UserRequest request) {
        return UserEntity.builder()
                .fullName(request.getFullName())
                .address(request.getAddress())
                .telephone(request.getTelephone())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
    }

    public static UserResponse mapTODto(UserEntity entity) {
        return UserResponse.builder()
                .id(entity.getId())
                .fullName(entity.getFullName())
                .address(entity.getAddress())
                .telephone(entity.getTelephone())
                .email(entity.getEmail())
                .roles(entity.getRoles())
                .build();
    }
}
