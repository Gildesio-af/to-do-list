package com.todo.list.filters;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.todo.list.entities.User;
import com.todo.list.services.TokenService;
import com.todo.list.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, ServletException, IOException {
        var authorizationHeader = request.getHeader("Authorization");

        var isRouteToCreateUser = request.getRequestURI().contains("/users") && request.getMethod().equals("POST");
        var isRouteToAuthenticate =
                request.getRequestURI().contains("/auth") && request.getMethod().equals("POST");

        if(isRouteToCreateUser || isRouteToAuthenticate){
            filterChain.doFilter(request, response);
            return;
        }

        if(!validateAuthorizationHeader(authorizationHeader)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization header is invalid");
            return;
        }

        String token = getToken(authorizationHeader);

        if(!tokenService.tokenIsValid(token)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is invalid");
            return;
        }

        DecodedJWT decodedJWT = tokenService.decodeToken(token);

        String userId = decodedJWT.getSubject();

        User user = userService.findById(userId);

        setAuthenticationContext(request, user);

        filterChain.doFilter(request, response);
    }

    private boolean validateAuthorizationHeader(String authorizationHeader){
        return authorizationHeader != null && authorizationHeader.startsWith("Bearer ");
    }

    private String getToken(String authorizationHeader){
        return authorizationHeader.substring(7);
    }

    private void setAuthenticationContext(HttpServletRequest request, User user){
        request.setAttribute("id", user.getId());
        var authorization = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authorization);
    }

}
