package com.bookbridge.bookbridge;

import com.bookbridge.bookbridge.repository.StudentRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.security.MessageDigest;
import java.util.Base64;

@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService(
            StudentRepository repository) {

        return email -> {
            Student student = repository.findByEmail(email)
                .orElseThrow(() ->
                    new org.springframework.security.core.userdetails
                        .UsernameNotFoundException("User not found"));

            return User.withUsername(student.getEmail())
                .password(student.getPasswordHash())
                .roles(student.getRole())
                .build();
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                throw new UnsupportedOperationException(
                    "Use existing registration password hashing");
            }

            @Override
            public boolean matches(
                    CharSequence rawPassword, String storedPassword) {
                try {
                    String[] parts = storedPassword.split(":");
                    byte[] salt = Base64.getDecoder().decode(parts[0]);

                    var spec = new javax.crypto.spec.PBEKeySpec(
                        rawPassword.toString().toCharArray(),
                        salt, 120000, 256
                    );

                    byte[] hash = javax.crypto.SecretKeyFactory
                        .getInstance("PBKDF2WithHmacSHA256")
                        .generateSecret(spec).getEncoded();

                    byte[] original =
                        Base64.getDecoder().decode(parts[1]);

                    return MessageDigest.isEqual(hash, original);
                } catch (Exception e) {
                    return false;
                }
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/students/register",
                    "/api/students/login"
                ).permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().permitAll()
            )
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}