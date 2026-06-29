package it.dnd.user_service.entity;

/**
 * Ruoli applicativi di un utente.
 * Volutamente SENZA prefisso "ROLE_": il prefisso viene aggiunto solo quando si costruiscono
 * le authorities di Spring Security / i claim del JWT, mantenendo l'enum pulito.
 */
public enum Role {
    USER,
    ADMIN
}