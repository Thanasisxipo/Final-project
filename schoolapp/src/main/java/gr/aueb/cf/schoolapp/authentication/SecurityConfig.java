package gr.aueb.cf.schoolapp.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Security configuration class for setting up authentication, authorization, and CORS configurations.
 */
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Defines the CORS configuration source.
     *
     * @return the CORS configuration source
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Defines the security filter chain.
     *
     * @param http the HttpSecurity instance to configure
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors
                        .configurationSource(corsConfigurationSource())
                )
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                                        .requestMatchers("/v3/api-docs/**",
                                                "/swagger-resources/**",
                                                "/swagger-ui/**",
                                                "/swagger-ui.html",
                                                "/api/login/",
                                                "/api/register/",
                                                "/api/register-student",
                                                "/api/register-teacher").permitAll()
                                        .requestMatchers("/api/students/**").hasAnyAuthority("ADMIN", "STUDENT")
                                        .requestMatchers("/api/teachers/**").hasAnyAuthority("TEACHER", "ADMIN")
                                        .requestMatchers("/api/cities/**").hasAnyAuthority("ADMIN", "STUDENT")
                                        .requestMatchers("/api/specialities/**").hasAnyAuthority("ADMIN", "TEACHER")
                                        .requestMatchers("/api/courses/**").hasAnyAuthority("ADMIN", "TEACHER")
                                        .requestMatchers("/api/users/**").hasAnyAuthority("ADMIN", "STUDENT", "TEACHER")
                                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}