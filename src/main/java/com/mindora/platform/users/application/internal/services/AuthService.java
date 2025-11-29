package com.mindora.platform.users.application.internal.services;

import com.mindora.platform.users.domain.model.aggregates.User;
import com.mindora.platform.users.domain.repositories.UserRepository;
import com.mindora.platform.users.infrastructure.security.JwtService;
import com.mindora.platform.users.interfaces.rest.resources.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public AuthResponseDto register(RegisterRequestDto request) {
        // Verificar si el email ya existe
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        // Crear nuevo usuario con password hasheado
        User user = new User(
                request.name(),
                request.email(),
                passwordEncoder.encode(request.password())
        );

        // Guardar usuario
        user = userRepository.save(user);

        // Generar JWT token
        String token = jwtService.generateToken(user.getId(), user.getEmail(), user.getName());

        // Retornar respuesta sin password
        UserDto userDto = new UserDto(user.getId(), user.getName(), user.getEmail());
        return new AuthResponseDto(token, userDto);
    }

    @Transactional(readOnly = true)
    public AuthResponseDto login(LoginRequestDto request) {
        // Buscar usuario por email
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Email o contraseña incorrectos"));

        // Verificar contraseña
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException("Email o contraseña incorrectos");
        }

        // Generar JWT token
        String token = jwtService.generateToken(user.getId(), user.getEmail(), user.getName());

        // Retornar respuesta sin password
        UserDto userDto = new UserDto(user.getId(), user.getName(), user.getEmail());
        return new AuthResponseDto(token, userDto);
    }
}

