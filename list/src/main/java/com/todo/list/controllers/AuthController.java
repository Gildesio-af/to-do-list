package com.todo.list.controllers;

import com.todo.list.dtos.LoginDTO;
import com.todo.list.dtos.TokenDTO;
import com.todo.list.services.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody LoginDTO loginDTO) {
        TokenDTO tokenDTO = loginService.authenticate(loginDTO.getEmail(), loginDTO.getPassword());
        return ResponseEntity.ok(tokenDTO);
    }

}
