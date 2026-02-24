package org.invoicebuilder.security.repository;

import org.invoicebuilder.security.domain.OAuthAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OAuthAccountRepository extends JpaRepository<OAuthAccount, UUID> {
    
    Optional<OAuthAccount> findByProviderAndProviderUserId(String provider, String providerUserId);
    
    @Query("SELECT oa FROM OAuthAccount oa WHERE oa.user.userId = :userId")
    List<OAuthAccount> findByUserId(@Param("userId") UUID userId);
}
