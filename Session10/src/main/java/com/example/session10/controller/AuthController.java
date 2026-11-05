package com.example.session10.controller;

import com.example.session10.dto.LoginRequest;
import com.example.session10.dto.LoginResponse;
import com.example.session10.dto.RegisterRequest;
import com.example.session10.dto.RegisterResponse;

import com.example.session10.security.JwtProvider;
import com.example.session10.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    public AuthController(
            AuthService authService,
            AuthenticationManager authenticationManager,
            JwtProvider jwtProvider) {

        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    // API Public
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok(
                "API Public hoạt động thành công"
        );
    }

    // Đăng ký tài khoản
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        RegisterResponse response = authService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // Đăng nhập và tạo JWT
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest request) {

        try {
            // 1. Tạo đối tượng xác thực
            Authentication authenticationRequest =
                    UsernamePasswordAuthenticationToken.unauthenticated(
                            request.getUsername(),
                            request.getPassword()
                    );

            // 2. Xác thực username và password
            Authentication authentication =
                    authenticationManager.authenticate(
                            authenticationRequest
                    );

            // 3. Tạo JWT sau khi xác thực thành công
            String token = jwtProvider.generateToken(authentication);

            // 4. Trả về response
            LoginResponse response = new LoginResponse(
                    authentication.getName(),
                    "Bearer",
                    token
            );

            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {

            // Username hoặc password không đúng
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "message", "username or password incorrect"
                    ));
        }
    }
}