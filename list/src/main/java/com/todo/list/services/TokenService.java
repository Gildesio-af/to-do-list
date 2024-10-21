package com.todo.list.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.todo.list.entities.Role;
import com.todo.list.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String secret;

    public String generatedToken(User user){
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withIssuer("ToDo")
                .withSubject(user.getId())
                .withClaim("roles",
                        user.getRoles().stream().map(Role::getAuthority).toList())
                .sign(algorithm);
    }

    public DecodedJWT decodeToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm).build().verify(token);
        } catch (Exception e){
            return null;
        }
    }

    public boolean tokenIsValid(String token){
        return decodeToken(token) != null;
    }

}
