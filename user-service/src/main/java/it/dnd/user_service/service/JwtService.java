package it.dnd.user_service.service;

import it.dnd.user_service.entity.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

/**
 * Emissione dei JWT firmati con algoritmo HMAC (HS256) e chiave simmetrica condivisa.
 * Tutti i servizi che conoscono la stessa chiave possono validare i token che produciamo qui.
 */
@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final String issuer;
    private final long expirationSeconds;

    public JwtService(JwtEncoder jwtEncoder,
                      @Value("${security.jwt.issuer}") String issuer,
                      @Value("${security.jwt.expiration-seconds}") long expirationSeconds) {
        this.jwtEncoder = jwtEncoder;
        this.issuer = issuer;
        this.expirationSeconds = expirationSeconds;
    }

    /**
     * Genera un token per l'utente indicato.
     * Claim principali: sub (username), iss (chi lo emette), iat/exp (validita'),
     * roles (lista di authorities, gia' col prefisso "ROLE_").
     */
    public String generateToken(String username, Role role) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expirationSeconds))
                .subject(username)
                .claim("roles", List.of("ROLE_" + role.name()))
                .build();

        // Va specificato HS256 nell'header, altrimenti l'encoder userebbe per default un algoritmo asimmetrico (RS256).
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    public long getExpirationSeconds() {
        return expirationSeconds;
    }
}