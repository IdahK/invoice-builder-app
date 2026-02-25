package org.invoicebuilder.security.service;

import org.invoicebuilder.users.domain.User;
import org.invoicebuilder.security.user.AuthUser;
import org.invoicebuilder.users.service.core.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User foundUser = userService.getUser(username);
        return new AuthUser(foundUser);
    }

}
