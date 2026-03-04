package org.invoicebuilder.users.repository;

import org.invoicebuilder.users.domain.UserRole;
import org.invoicebuilder.users.domain.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {

    List<UserRole> findByUser_UserId(UUID userId);

    boolean existsById_UserIdAndId_RoleId(UUID userId, UUID roleId);

    @Query("SELECT ur.role.roleName FROM UserRole ur WHERE ur.user.userId = :userId")
    List<String> findRoleNamesByUserId(@Param("userId") UUID userId);

    @Query("SELECT ur.user FROM UserRole ur WHERE ur.role.roleName = :roleName")
    List<org.invoicebuilder.users.domain.User> findUsersByRoleName(@Param("roleName") String roleName);

    void deleteById_UserIdAndId_RoleId(UUID userId, UUID roleId);
}
