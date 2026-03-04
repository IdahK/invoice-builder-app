package org.invoicebuilder.users.repository;

import org.invoicebuilder.users.domain.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, UUID> {
    
    Optional<EmailVerificationToken> findByTokenHash(String tokenHash);
    
    @Query("SELECT evt FROM EmailVerificationToken evt WHERE evt.user.userId = :userId AND evt.used = false AND evt.expiresAt > :now")
    List<EmailVerificationToken> findValidTokensByUser(@Param("userId") UUID userId, @Param("now") Instant now);
    
    void deleteByTokenHash(String tokenHash);
}
