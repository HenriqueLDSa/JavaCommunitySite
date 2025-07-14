package com.jcs.javacommunitysite.service;

import com.jcs.javacommunitysite.dto.user.CreateUserRequest;
import com.jcs.javacommunitysite.dto.user.UserDTO;
import com.jcs.javacommunitysite.model.User;
import com.jcs.javacommunitysite.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO createUser(CreateUserRequest request) {
        if(userRepo.existsByEmailAndUsername(request.getEmail(), request.getUsername())){
            throw new IllegalArgumentException("Email or Username already in use");
        }

        String newPassword = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(newPassword)
                .build();

        User saved = userRepo.save(user);

        return UserDTO.builder()
                .id(saved.getId())
                .firstName(saved.getFirstName())
                .lastName(saved.getLastName())
                .username(saved.getUsername())
                .email(saved.getEmail())
                .build();
    }

}
