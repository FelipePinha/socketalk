package com.pinhadev.socketalk.service;

import com.pinhadev.socketalk.config.JwtService;
import com.pinhadev.socketalk.dto.AuthResponse;
import com.pinhadev.socketalk.dto.LoginRequest;
import com.pinhadev.socketalk.dto.RegisterRequest;
import com.pinhadev.socketalk.exception.ConflictException;
import com.pinhadev.socketalk.exception.ResourceNotFoundException;
import com.pinhadev.socketalk.exception.UnauthorizedException;
import com.pinhadev.socketalk.model.User;
import com.pinhadev.socketalk.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    private static final String INVALID_CREDENTIALS = "Email ou senha inválidos";

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder encoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtService = jwtService;
    }

    public AuthResponse registerUser(RegisterRequest request) {
        var existsUsername = userRepository.existsByUsername(request.username());
        var existsEmail = userRepository.existsByEmail(request.email());

        if(existsUsername) {
            throw new ConflictException("Nome de usuário já está em uso");
        }

        if(existsEmail) {
            throw new ConflictException("Já existe uma conta com este email");
        }

        var passwordHash = encoder.encode(request.password());

        var user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordHash)
                .build();

        userRepository.save(user);
        var token = jwtService.generateToken(user.getEmail());

        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest request) {
        var user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UnauthorizedException(INVALID_CREDENTIALS));

        if (!encoder.matches(request.password(), user.getPassword())) {
            throw new UnauthorizedException(INVALID_CREDENTIALS);
        }

        var token = jwtService.generateToken(user.getEmail());

        return new AuthResponse(token);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws ResourceNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }
}
