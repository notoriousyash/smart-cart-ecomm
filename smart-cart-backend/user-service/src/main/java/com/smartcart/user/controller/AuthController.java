package com.smartcart.user.controller;

import com.smartcart.user.dto.AuthRequestDTO;
import com.smartcart.user.dto.AuthResponseDTO;
import com.smartcart.user.dto.RegisterRequestDTO;
import com.smartcart.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<java.util.Map<String, String>> register(@Valid @RequestBody RegisterRequestDTO request) {
        String response = authService.register(request);
        return new ResponseEntity<>(java.util.Map.of("message", response), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO request) {
        AuthResponseDTO response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
