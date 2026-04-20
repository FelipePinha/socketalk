package com.pinhadev.socketalk.service;

import com.pinhadev.socketalk.dto.AuthResponse;
import com.pinhadev.socketalk.dto.LoginRequest;
import com.pinhadev.socketalk.dto.RegisterRequest;
import com.pinhadev.socketalk.model.User;
import com.pinhadev.socketalk.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final String INVALID_CREDENTIALS = "Email ou senha inválidos";

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public AuthResponse registerUser(RegisterRequest request) {
        var existsUsername = userRepository.existsByUsername(request.username());
        var existsEmail = userRepository.existsByEmail(request.email());

        if(existsUsername) {
            throw new RuntimeException("Nome de usuário já está em uso");
        }

        if(existsEmail) {
            throw new RuntimeException("Já existe uma conta com este email");
        }

        var passwordHash = encoder.encode(request.password());

        var user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordHash)
                .build();

        userRepository.save(user);

        return new AuthResponse("token");
    }

    public AuthResponse login(LoginRequest request) {
        var user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException(INVALID_CREDENTIALS));

        if (!encoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException(INVALID_CREDENTIALS);
        }

        return new AuthResponse("token");
    }
}
