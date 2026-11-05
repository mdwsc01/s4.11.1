package com.example.session10.dto;

public record RegisterResponse(
        Long id,
        String username,
        String fullName,
        String role,
        boolean enabled
) {
}