package com.todo.list.dtos;

import com.todo.list.entities.Role;
import com.todo.list.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Set;

public record UserCreateDTO(
        @NotBlank(message = "O campo nome não pode estar vazio")
        String name,
        @NotBlank(message = "O campo email não pode estar vazio")
        @Email(message = "O email deve ser válido")
        String email,
        @Length(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
        String password,
        List<RoleDTO> roles
) {
    public User toEntity() {
        return User.builder().name(name).email(email)
                .password(password)
                .createdAt(Timestamp.from(Instant.now()))
                .updatedAt(Timestamp.from(Instant.now()))
                .build();
    }
}
