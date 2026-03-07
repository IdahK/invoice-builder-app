package org.invoicebuilder.users.service;

import lombok.RequiredArgsConstructor;
import org.invoicebuilder.users.domain.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final OAuthService oAuthService;
    private final UserRoleService userRoleService;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauthUser = super.loadUser(userRequest);

        User user = oAuthService.processOauthUser(oauthUser);
        
        // Get user's assigned roles
        List<String> roleNames = userRoleService.getUserRoleNames(user.getUserId());
        
        // Convert role names to GrantedAuthority
        List<SimpleGrantedAuthority> authorities = roleNames.stream()
                .map(roleName -> new SimpleGrantedAuthority("ROLE_" + roleName))
                .toList();

        return new DefaultOAuth2User(
                authorities,
                oauthUser.getAttributes(),
                "email"
        );
    }

}
