package com.example.session10.dto;

public record LoginResponse(
        String username,
        String type,
        String accessToken
) {
}