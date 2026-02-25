package org.invoicebuilder.users.service.core;

import org.invoicebuilder.users.domain.User;
import org.invoicebuilder.users.domain.UserStatus;
import org.invoicebuilder.users.dto.UserDtos.CreateUserRequest;
import org.invoicebuilder.users.repository.RoleRepository;
import org.invoicebuilder.users.repository.UserRepository;
import org.invoicebuilder.users.service.core.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service responsible for user management operations
 * Follows Single Responsibility Principle and implements IUserService
 */
@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getUser(String username) {
        Optional<User> user = userRepository.findByUserEmail(username);

        if(user.isEmpty())
            throw new UsernameNotFoundException("User " + username + " not found");

        User foundUser = user.get();
        return foundUser;
    }

    @Override
    @Transactional
    public User createUser(CreateUserRequest request) {
        User user = User.builder()
                .userEmail(request.email())
                .userPassword(passwordEncoder.encode(request.password()))
                .userDisplayName(request.displayName())
                .userEmailVerified(false)
                .userStatus(UserStatus.ACTIVE)
                .account(request.account())
                .build();

        return userRepository.save(user);
    }

    @Override
    public boolean userExistsByEmail(String email) {
        return userRepository.existsByUserEmail(email);
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByUserEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User " + email + " not found"));
    }
}
