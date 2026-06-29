package it.dnd.user_service.security;

import it.dnd.user_service.entity.User;
import it.dnd.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Ponte tra la nostra entita' {@link User} e il modello utente di Spring Security.
 * Viene usato dal DaoAuthenticationProvider (login con username/password) per caricare
 * l'utente dal DB e verificarne la password tramite il PasswordEncoder.
 */
@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato: " + username));

        // Costruiamo l'UserDetails standard di Spring; l'authority usa il prefisso convenzionale "ROLE_".
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities("ROLE_" + user.getRole().name())
                .disabled(!user.isEnabled())
                .build();
    }
}