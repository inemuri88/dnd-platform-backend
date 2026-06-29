package it.dnd.user_service.dto;

/**
 * Risposta restituita dopo registrazione/login.
 *
 * @param accessToken   il JWT firmato da inviare nelle richieste successive come header
 *                      "Authorization: Bearer &lt;accessToken&gt;"
 * @param tokenType     sempre "Bearer"
 * @param expiresIn     durata residua del token in secondi
 * @param username      username autenticato
 * @param role          ruolo dell'utente
 */
public record AuthResponse(
        String accessToken,
        String tokenType,
        long expiresIn,
        String username,
        String role
) {
}