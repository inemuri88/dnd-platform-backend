package it.dnd.user_service.dto;

import jakarta.validation.constraints.NotBlank;

/** Payload della richiesta di login. */
public record LoginRequest(

        @NotBlank(message = "username obbligatorio")
        String username,

        @NotBlank(message = "password obbligatoria")
        String password
) {
}