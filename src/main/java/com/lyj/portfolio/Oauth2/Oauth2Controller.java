package com.lyj.portfolio.Oauth2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lyj.portfolio.CurrentAccount;
import com.lyj.portfolio.VO.Account;
import com.lyj.portfolio.account.AccountService;
import com.lyj.portfolio.account.SignUpForm;
import com.lyj.portfolio.mapper.AccountMapper;
import com.lyj.portfolio.validator.SignUpFormValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class Oauth2Controller {
    private final AccountMapper accountMapper;
    private final PasswordEncoder passwordEncoder;
    private final Oauth2Service oauth2Service;

    //임시로 아이디 생성
    @GetMapping("/generate")
    public String newAccount() {
        Account account=Account.builder()
                .name("이영재")
                .user_id("lyj6752")
                .password(passwordEncoder.encode("123123123"))
                .email("lyj6752@naver.com").build();
        accountMapper.save(account);
        return "index";
    }

    @GetMapping("/google-callback")
    public String loginToGoogle() {
        return "index";
    }
}
