package com.todo.list.dtos;

import com.todo.list.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoleDTO{
    private String id;
    private String authority;

    public static RoleDTO fromEntity(Role role) {
        return new RoleDTO(role.getId(), role.getAuthority());
    }
}
