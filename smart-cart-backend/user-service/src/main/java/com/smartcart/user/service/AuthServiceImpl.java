package com.smartcart.user.service;

import com.smartcart.user.dto.AuthRequestDTO;
import com.smartcart.user.dto.AuthResponseDTO;
import com.smartcart.user.dto.RegisterRequestDTO;
import com.smartcart.user.entity.User;
import com.smartcart.user.repository.UserRepository;
import com.smartcart.user.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public String register(RegisterRequestDTO request) {
        Optional<User> existingUser = userRepository.findByUsername(request.getUsername());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole() != null ? request.getRole() : "USER")
                .build();

        userRepository.save(user);
        return "User registered successfully";
    }

    @Override
    public AuthResponseDTO login(AuthRequestDTO request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Bad credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Bad credentials");
        }

        String token = jwtService.generateToken(user.getUsername(), user.getRole());
        return AuthResponseDTO.builder()
                .token(token)
                .message("Login successful")
                .userId(user.getId())
                .build();
    }
}
