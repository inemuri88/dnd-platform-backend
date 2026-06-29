package it.dnd.user_service.repository;

import it.dnd.user_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Accesso ai dati degli utenti. Spring Data genera le query dai nomi dei metodi.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /** Usato in fase di login e dal UserDetailsService per caricare l'utente. */
    Optional<User> findByUsername(String username);

    /** Controlli di unicita' in fase di registrazione. */
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}