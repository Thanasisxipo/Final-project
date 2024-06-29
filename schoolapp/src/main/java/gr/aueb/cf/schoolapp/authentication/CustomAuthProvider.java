package gr.aueb.cf.schoolapp.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration class for custom authentication providers and related beans.
 * This class sets up the authentication provider, authentication manager,
 * and password encoder for the application.
 */
@Configuration
@RequiredArgsConstructor
public class CustomAuthProvider {

    private final CustomUserDetailsService userDetailsService;

    /**
     * Defines the authentication provider bean.
     * Uses {@link DaoAuthenticationProvider} with a custom user details service and password encoder.
     *
     * @return the authentication provider
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Defines the authentication manager bean.
     * Retrieves the authentication manager from the provided {@link AuthenticationConfiguration}.
     *
     * @param config the authentication configuration
     * @return the authentication manager
     * @throws Exception if an error occurs while retrieving the authentication manager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Defines the password encoder bean.
     * Uses {@link BCryptPasswordEncoder} for encoding passwords.
     *
     * @return the password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
