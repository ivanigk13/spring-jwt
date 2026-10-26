package com.example.demo.service;

import com.example.demo.config.JwtUtil;
import com.example.demo.entity.UserEntity;
import com.example.demo.entity.dto.AuthRequest;
import com.example.demo.entity.dto.AuthResponse;
import com.example.demo.entity.dto.RegisterRequest;
import com.example.demo.entity.dto.RegisterResponse;
import com.example.demo.exception.UserException;
import com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        if(userRepository.existsByUsername(request.getUsername())) {
            throw new UserException("Username is already in use");
        }

        UserEntity user = UserEntity.builder()
            .username(request.getUsername())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword())) // WAJIB di-hash, jangan plain text
            .role("ROLE_USER")
            .build();

        UserEntity createdUser = userRepository.save(user);
        emailService.sendEmail(createdUser.getEmail(), "Welcome " + createdUser.getUsername());
        return new RegisterResponse(createdUser.getId());
    }

    public AuthResponse login(AuthRequest request) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (AuthenticationException e) {
            log.error("Authentication failed for username={}", request.getUsername(), e);
            throw new BadCredentialsException("Username atau password salah");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        UserEntity user = userRepository.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new BadCredentialsException("Username atau password salah"));

        Map<String, Object> extraClaims = Map.of("email", user.getEmail());

        String token = jwtUtil.generateToken(extraClaims, userDetails);
        return new AuthResponse(token);
    }
}
