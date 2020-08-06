package com.example.redditclone.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
@Builder
public class AuthenticationResponse {
    private String username;
    private String jwtToken;
    private String refreshToken;
    private Instant expireAt;
}
