package com.todo.list.services;

import com.todo.list.dtos.TokenDTO;
import com.todo.list.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final TokenService tokenService;

    private final AuthenticationManager authenticationManager;

    public TokenDTO authenticate(String email, String password) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        User user = (User) authentication.getPrincipal();

        String token = tokenService.generatedToken(user);
        return new TokenDTO(token);
    }

}
