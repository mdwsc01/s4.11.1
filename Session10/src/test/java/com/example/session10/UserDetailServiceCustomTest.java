package com.example.session10.security;

import com.example.session10.entity.User;
import com.example.session10.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserDetailServiceCustomTest {

    @Autowired
    private UserDetailServiceCustom userDetailServiceCustom;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void testLoadUserSuccess() {

        User user = new User();

        user.setUsername("test_user_bai4");
        user.setPassword(passwordEncoder.encode("123456"));
        user.setRole("USER");
        user.setEnabled(true);

        userRepository.saveAndFlush(user);

        UserDetails result = userDetailServiceCustom
                .loadUserByUsername("test_user_bai4");

        assertInstanceOf(UserPrincipal.class, result);
        assertEquals("test_user_bai4", result.getUsername());
        assertTrue(result.isEnabled());

        assertTrue(
                result.getAuthorities().stream()
                        .anyMatch(a ->
                                a.getAuthority().equals("ROLE_USER"))
        );

        assertTrue(
                passwordEncoder.matches(
                        "123456",
                        result.getPassword()
                )
        );
    }

    @Test
    void testUserNotFound() {

        assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailServiceCustom
                        .loadUserByUsername("unknown_bai4_987654")
        );
    }
}