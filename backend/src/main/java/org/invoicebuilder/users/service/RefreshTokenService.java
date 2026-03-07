package org.invoicebuilder.users.service;

import lombok.RequiredArgsConstructor;
import org.invoicebuilder.config.TokenProperties;
import org.invoicebuilder.users.domain.RefreshToken;
import org.invoicebuilder.users.domain.User;
import org.invoicebuilder.users.repository.RefreshTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private static final Logger LOG = LoggerFactory.getLogger(RefreshTokenService.class);

    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProperties tokenProperties;

    public String createToken(User user){
        String rawToken = generateSecureToken();
        String tokenHash = hashToken(rawToken);

        RefreshToken refreshToken = RefreshToken
                .builder()
                .user(user)
                .tokenHash(tokenHash)
                .expiresAt(Instant.now().plus(tokenProperties.getRefreshTokenTtl().getSeconds(), ChronoUnit.SECONDS))
                .revoked(false)
                .build();
        refreshTokenRepository.save(refreshToken);
        return rawToken;
    }

    private String generateSecureToken(){
        byte[] randomBytes = new byte[64];
        new SecureRandom().nextBytes(randomBytes);

        return Base64.getUrlEncoder().encodeToString(randomBytes);
    }

    private String hashToken(String token){
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(
                    token.getBytes(StandardCharsets.UTF_8)
            );
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            LOG.error("SHA-256 Algorithm not available", e);
            throw new IllegalStateException("Problem with token.Please try again");
        }
    }


}
