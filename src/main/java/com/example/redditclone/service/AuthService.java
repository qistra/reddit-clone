package com.example.redditclone.service;

import com.example.redditclone.dto.RegisterRequest;
import com.example.redditclone.model.User;
import com.example.redditclone.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
public class AuthService {

    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;

    public void signup(RegisterRequest registerRequest) {
        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .created(Instant.now())
                .enabled(true)
                .build();

        userRepository.save(user);
    }
}
