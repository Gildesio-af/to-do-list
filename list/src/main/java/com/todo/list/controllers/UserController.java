package com.todo.list.controllers;

import com.todo.list.dtos.UserCreateDTO;
import com.todo.list.dtos.UserResponseDTO;
import com.todo.list.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable String id) {
        return ResponseEntity.ok().body(userService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO>  createUser(@RequestBody UserCreateDTO userCreate) {
        UserResponseDTO userResponse = userService.createUser(userCreate);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id").buildAndExpand(userResponse.id()).toUri();
        return ResponseEntity.created(location).body(userResponse);
    }
}
