package com.todo.list.dtos;

import com.todo.list.entities.User;

import java.util.List;

public record UserResponseDTO(
        String id,
        String name,
        String email,
        List<RoleDTO> roles
) {
    public static UserResponseDTO fromEntity(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRoles().stream().map(RoleDTO::fromEntity).toList()
        );
    }
}
