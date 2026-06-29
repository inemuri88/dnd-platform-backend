package it.dnd.user_service.controller;

import it.dnd.user_service.dto.AuthResponse;
import it.dnd.user_service.dto.LoginRequest;
import it.dnd.user_service.dto.RegisterRequest;
import it.dnd.user_service.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint pubblici di autenticazione (vedi SecurityConfig: /api/auth/** e' permitAll).
 * Esposto dal gateway sul path /api/auth/**.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /** Registrazione di un nuovo utente; risponde 201 con il token. */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    /** Login con username/password; risponde 200 con il token. */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}