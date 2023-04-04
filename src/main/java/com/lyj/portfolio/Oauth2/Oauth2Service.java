package com.lyj.portfolio.Oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lyj.portfolio.VO.Account;
import com.lyj.portfolio.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class Oauth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final AccountMapper accountMapper;
    private final HttpSession httpSession;

    @SneakyThrows
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(userRequest));
        System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(oAuth2User));


        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        Account account = saveOrUpdate(attributes);
        httpSession.setAttribute("user", new SessionUser(account));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(account.getRoleValue())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    private Account saveOrUpdate(OAuthAttributes attributes) {
        Account account = accountMapper.findByEmail(attributes.getEmail());
        if(account.getUser_id() != null) {
            Account updatedAccount = account.update(attributes.getName());
            return accountMapper.updateForOauth(updatedAccount);
        }else {
            Account newAccount = Account.builder()
                    .user_id(attributes.getName())
                    .name("new_User")
                    .email(attributes.getEmail())
                    .role(Role.USER)
                    .build();
            return accountMapper.saveForOauth(newAccount);
        }
    }

}
