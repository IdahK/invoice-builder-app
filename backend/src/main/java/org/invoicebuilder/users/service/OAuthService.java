package org.invoicebuilder.users.service;

import lombok.RequiredArgsConstructor;
import org.invoicebuilder.users.domain.User;
import org.invoicebuilder.users.repository.UserRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final UserRepository userRepository;

    public User processOauthUser(OAuth2User oathOAuth2User){
        String email = oathOAuth2User.getAttribute("email");

        return userRepository.findByUserEmail(email)
                .orElseGet(
                        () -> {
                            User user = new User();
                            user.setUserEmail(email);
                            return userRepository.save(user);
                        }
                );
    }

}
