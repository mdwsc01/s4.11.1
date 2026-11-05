package com.example.session10.service;

import com.example.session10.dto.RegisterRequest;
import com.example.session10.dto.RegisterResponse;
import com.example.session10.entity.User;
import com.example.session10.exception.DuplicateUsernameException;
import com.example.session10.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public RegisterResponse register(RegisterRequest dto) {

        // 1. Kiểm tra username đã tồn tại
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new DuplicateUsernameException(
                    "Username đã tồn tại"
            );
        }

        // 2. Tạo tài khoản
        User user = new User();

        user.setUsername(dto.getUsername());
        user.setFullName(dto.getFullName());

        // 3. Mã hóa mật khẩu trước khi lưu
        user.setPassword(
                passwordEncoder.encode(dto.getPassword())
        );

        // 4. Gán role và trạng thái mặc định
        user.setRole("USER");
        user.setEnabled(true);

        // 5. Lưu vào MySQL
        User saved;

        try {
            saved = userRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateUsernameException(
                    "Username đã tồn tại"
            );
        }

        // 6. Trả về DTO không chứa password
        return new RegisterResponse(
                saved.getId(),
                saved.getUsername(),
                saved.getFullName(),
                saved.getRole(),
                saved.isEnabled()
        );
    }
}