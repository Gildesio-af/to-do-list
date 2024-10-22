package com.todo.list.services;

import com.todo.list.dtos.UserCreateDTO;
import com.todo.list.dtos.UserResponseDTO;
import com.todo.list.entities.Role;
import com.todo.list.entities.User;
import com.todo.list.repositories.RoleRepository;
import com.todo.list.repositories.UserRepository;
import jakarta.persistence.SecondaryTable;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmailWithRoles(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

    }

    public User findById(String id){
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + id));
    }

    public UserResponseDTO getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + id));
        return UserResponseDTO.fromEntity(user);
    }
    public UserResponseDTO createUser(UserCreateDTO userDTO){
        User user = userDTO.toEntity();
        Set<Role> roles = userDTO.roles().stream().map(
                roleDTO -> roleRepository.findByAuthority(roleDTO.getAuthority())
                        .orElseThrow(() -> new UsernameNotFoundException("Role not found: " + roleDTO.getAuthority())
                        )).collect(Collectors.toSet());
        user.setRoles(roles);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        return UserResponseDTO.fromEntity(userRepository.save(user));
    }
}
