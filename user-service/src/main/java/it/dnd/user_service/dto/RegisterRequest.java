package it.dnd.user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Payload della richiesta di registrazione. I vincoli @NotBlank/@Email/@Size
 * vengono validati automaticamente grazie a @Valid nel controller.
 */
public record RegisterRequest(

        @NotBlank(message = "username obbligatorio")
        @Size(min = 3, max = 50, message = "username tra 3 e 50 caratteri")
        String username,

        @NotBlank(message = "email obbligatoria")
        @Email(message = "email non valida")
        String email,

        @NotBlank(message = "password obbligatoria")
        @Size(min = 8, max = 100, message = "password di almeno 8 caratteri")
        String password
) {
}