package com.lyj.portfolio.Oauth2;

import com.lyj.portfolio.VO.Account;
import com.lyj.portfolio.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class Oauth2Controller {
    private final AccountMapper accountMapper;
    private final PasswordEncoder passwordEncoder;
    private final CustomOAuth2UserService customOAuth2UserService;

    //임시로 아이디 생성
    @GetMapping("/generate")
    public String newAccount(Model model) {
        Account account=Account.builder()
                .name("이영재")
                .user_id("lyj6752")
                .password(passwordEncoder.encode("123123123"))
                .email("lyj6752@naver.com").build();
        accountMapper.save(account);
        model.addAttribute("account",new Account());
        return "index";
    }
}
