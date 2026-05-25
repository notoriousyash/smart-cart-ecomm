package com.smartcart.user.service;

import com.smartcart.user.dto.AuthRequestDTO;
import com.smartcart.user.dto.AuthResponseDTO;
import com.smartcart.user.dto.RegisterRequestDTO;

public interface AuthService {
    String register(RegisterRequestDTO request);
    AuthResponseDTO login(AuthRequestDTO request);
}
