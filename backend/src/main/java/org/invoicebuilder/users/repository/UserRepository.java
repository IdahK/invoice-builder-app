package org.invoicebuilder.users.repository;

import org.invoicebuilder.users.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    
    Optional<User> findByUserEmail(String email);
    
    boolean existsByUserEmail(String email);

    boolean existsByUserEmailAndUserEmailVerifiedTrue(String userEmail, Boolean userEmailVerified);
    
    @Query("SELECT u FROM User u WHERE u.userEmail = :email AND u.userEmailVerified = true")
    Optional<User> findByEmailAndVerified(@Param("email") String email);
    
    @Query("SELECT u FROM User u WHERE u.account.accountId = :accountId")
    List<User> findByAccountId(@Param("accountId") UUID accountId);
}
