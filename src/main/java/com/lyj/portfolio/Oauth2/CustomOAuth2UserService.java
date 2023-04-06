package com.lyj.portfolio.Oauth2;

import com.lyj.portfolio.VO.Account;
import com.lyj.portfolio.login.AccountAdapter;
import com.lyj.portfolio.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final AccountMapper accountMapper;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        Account account = saveOrUpdate(attributes);
        httpSession.setAttribute("user", new SessionUser(account));

        return new AccountAdapter(account, attributes.getAttributes());
    }

    private Account saveOrUpdate(OAuthAttributes attributes) {
        Account account = accountMapper.findByEmail(attributes.getEmail());
        if(account != null) {
            Account updatedAccount = account.update(attributes.getName());
            accountMapper.updateForOauth(updatedAccount);
            return updatedAccount;
        }else {
            Account newAccount = attributes.toEntity();
            accountMapper.saveForOauth(newAccount);
            return newAccount;
        }
    }

}
