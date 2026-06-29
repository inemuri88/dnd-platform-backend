package it.dnd.user_service.security;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * Configurazione di sicurezza dello user-service.
 *
 * Lo user-service ha un doppio ruolo:
 *  - EMETTE i token (endpoint /api/auth/** pubblici) tramite il {@link JwtEncoder};
 *  - e' anche un RESOURCE SERVER per i propri endpoint protetti, validando i token col {@link JwtDecoder}.
 *
 * L'autenticazione e' stateless: nessuna sessione HTTP, ogni richiesta porta il proprio JWT.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity // abilita @PreAuthorize/@Secured sui metodi, per autorizzazioni granulari
public class SecurityConfig {

    @Value("${security.jwt.secret}")
    private String jwtSecret;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationConverter jwtAuthConverter) throws Exception {
        http
                // API stateless consumate da client non-browser: CSRF non serve e va disattivato.
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Registrazione e login devono essere raggiungibili senza token.
                        .requestMatchers("/api/auth/**").permitAll()
                        // Endpoint di monitoraggio liberi (in prod andrebbero ristretti).
                        .requestMatchers("/actuator/**").permitAll()
                        // Tutto il resto richiede un JWT valido.
                        .anyRequest().authenticated())
                // Attiva la validazione dei JWT in ingresso con il nostro converter dei ruoli.
                .oauth2ResourceServer(oauth -> oauth.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter)));
        return http.build();
    }

    /** Hashing delle password con BCrypt (salt incluso, resistente a brute-force). */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * AuthenticationManager usato in fase di login: Spring Boot lo costruisce a partire dal
     * nostro UserDetailsService + PasswordEncoder (DaoAuthenticationProvider auto-configurato).
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /** Chiave simmetrica HMAC ricavata dal segreto condiviso. */
    private SecretKey hmacKey() {
        return new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    }

    /** Encoder per FIRMARE i token al login (HS256). */
    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(hmacKey()));
    }

    /** Decoder per VALIDARE i token sugli endpoint protetti di questo servizio. */
    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withSecretKey(hmacKey())
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

    /**
     * Mappa il claim "roles" del JWT in authorities di Spring Security.
     * I valori sono gia' del tipo "ROLE_USER", quindi azzeriamo il prefisso aggiunto di default.
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authorities = new JwtGrantedAuthoritiesConverter();
        authorities.setAuthoritiesClaimName("roles");
        authorities.setAuthorityPrefix("");

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authorities);
        return converter;
    }
}