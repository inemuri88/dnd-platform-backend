package it.dnd.user_service.service;

import it.dnd.user_service.dto.AuthResponse;
import it.dnd.user_service.dto.LoginRequest;
import it.dnd.user_service.dto.RegisterRequest;
import it.dnd.user_service.entity.Role;
import it.dnd.user_service.entity.User;
import it.dnd.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

/**
 * Logica di registrazione e login. Restituisce sempre un {@link AuthResponse} con un JWT valido.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    /**
     * Crea un nuovo utente (ruolo USER) dopo aver verificato l'unicita' di username/email
     * e aver cifrato la password. Restituisce gia' un token per loggare l'utente all'istante.
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username gia' in uso");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email gia' in uso");
        }

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .enabled(true)
                .build();
        userRepository.save(user);

        return buildResponse(user);
    }

    /**
     * Verifica le credenziali tramite l'AuthenticationManager (che usa UserDetailsService + BCrypt).
     * Se non combaciano, Spring lancia una BadCredentialsException -> 401.
     */
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenziali non valide"));

        return buildResponse(user);
    }

    private AuthResponse buildResponse(User user) {
        String token = jwtService.generateToken(user.getUsername(), user.getRole());
        return new AuthResponse(
                token,
                "Bearer",
                jwtService.getExpirationSeconds(),
                user.getUsername(),
                user.getRole().name());
    }
}