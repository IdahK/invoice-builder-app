package org.invoicebuilder.security.service;

import jakarta.validation.constraints.NotNull;
import org.invoicebuilder.security.dto.AuthResponse;
import org.invoicebuilder.security.dto.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service responsible for authentication orchestration
 * Now delegates registration to RegistrationService for better maintainability
 */
@Service
public class AuthService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthResponse login(@NotNull LoginRequest loginRequest){
        String email = loginRequest.email();
        String passwordHash = passwordEncoder.encode(loginRequest.password());
    }


}
