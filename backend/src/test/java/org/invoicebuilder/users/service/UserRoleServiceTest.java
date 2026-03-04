package org.invoicebuilder.users.service;

import org.invoicebuilder.users.domain.Role;
import org.invoicebuilder.users.domain.User;
import org.invoicebuilder.users.domain.UserRole;
import org.invoicebuilder.users.repository.UserRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRoleServiceTest {

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private UserRoleService userRoleService;

    private User testUser;
    private Role ownerRole;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .userId(UUID.randomUUID())
                .userEmail("test@example.com")
                .userDisplayName("Test User")
                .build();

        ownerRole = Role.builder()
                .roleId(UUID.randomUUID())
                .roleName("OWNER")
                .build();
    }

    @Test
    void testAssignRoleToUser_Success() {
        // Given
        when(roleService.findRoleByName("OWNER")).thenReturn(ownerRole);
        when(userRoleRepository.existsById_UserIdAndId_RoleId(testUser.getUserId(), ownerRole.getRoleId()))
                .thenReturn(false);
        when(userRoleRepository.save(any(UserRole.class))).thenReturn(new UserRole(testUser, ownerRole));

        // When
        User result = userRoleService.assignRoleToUser(testUser, "OWNER");

        // Then
        assertNotNull(result);
        assertEquals(testUser, result);
        verify(userRoleRepository).save(any(UserRole.class));
        verify(roleService).findRoleByName("OWNER");
    }

    @Test
    void testAssignRoleToUser_AlreadyAssigned() {
        // Given
        when(roleService.findRoleByName("OWNER")).thenReturn(ownerRole);
        when(userRoleRepository.existsById_UserIdAndId_RoleId(testUser.getUserId(), ownerRole.getRoleId()))
                .thenReturn(true);

        // When
        User result = userRoleService.assignRoleToUser(testUser, "OWNER");

        // Then
        assertNotNull(result);
        assertEquals(testUser, result);
        verify(userRoleRepository, never()).save(any(UserRole.class)); // Should not save if role already assigned
    }

    @Test
    void testUserHasRole_True() {
        // Given
        when(roleService.findRoleByName("OWNER")).thenReturn(ownerRole);
        when(userRoleRepository.existsById_UserIdAndId_RoleId(testUser.getUserId(), ownerRole.getRoleId()))
                .thenReturn(true);

        // When
        boolean result = userRoleService.userHasRole(testUser, "OWNER");

        // Then
        assertTrue(result);
        verify(userRoleRepository).existsById_UserIdAndId_RoleId(testUser.getUserId(), ownerRole.getRoleId());
    }

    @Test
    void testUserHasRole_False() {
        // Given
        when(roleService.findRoleByName("OWNER")).thenReturn(ownerRole);
        when(userRoleRepository.existsById_UserIdAndId_RoleId(testUser.getUserId(), ownerRole.getRoleId()))
                .thenReturn(false);

        // When
        boolean result = userRoleService.userHasRole(testUser, "OWNER");

        // Then
        assertFalse(result);
        verify(userRoleRepository).existsById_UserIdAndId_RoleId(testUser.getUserId(), ownerRole.getRoleId());
    }
}
