package org.invoicebuilder.users.repository;

import org.invoicebuilder.users.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    
    Optional<RefreshToken> findByTokenHash(String tokenHash);
    
    @Query("SELECT rt FROM RefreshToken rt WHERE rt.user.userId = :userId AND rt.revoked = false AND rt.expiresAt > :now")
    java.util.List<RefreshToken> findValidTokensByUser(@Param("userId") UUID userId, @Param("now") Instant now);
    
    void deleteByTokenHash(String tokenHash);
}
