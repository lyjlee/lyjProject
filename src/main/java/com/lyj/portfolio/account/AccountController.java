package com.lyj.portfolio.account;

import com.lyj.portfolio.VO.Account;
import com.lyj.portfolio.mapper.AccountMapper;
import com.lyj.portfolio.validator.SignUpFormValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class AccountController {
    private final SignUpFormValidator signUpFormValidator;
    private final AccountService accountService;
    private final AccountMapper accountMapper;




    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpFormValidator);
    }

    @GetMapping("/check-email-token")
    public String checkEmailToken(String token, String email, Model model) {
        String view = "account/checked-Email";
        Account account = accountService.findByEmail(email);
        if(account == null) {
            model.addAttribute("error", "wrong.email");
            return view;
        }
        if(!account.isValidToken(token)) {
            model.addAttribute("error","wrong.token");
            return view;
        }
        accountService.finishNewAccount(account);
        accountService.login(account);
        model.addAttribute("name",account.getName());
        return view;
    }

    @GetMapping("/account/sign-up")
    public String sign_up(Model model) {
        model.addAttribute(new SignUpForm());
        return "account/sign-up";
    }


    @PostMapping("/account/sign-up")
    public String post(@Valid @ModelAttribute SignUpForm signUpForm, Errors errors, Model model) {
        if(errors.hasFieldErrors("user_id")) {
            return "account/sign-up";
        }
        Account account = accountService.processNewAccount(signUpForm);
        accountService.login(account);
        model.addAttribute("account",account);
        return "redirect:/";
    }


    //비밀번호 찾기 관련 핸들러

    @GetMapping("/account/find-password")
    public String find_password() {
        return "account/find-password";
    }

    @PostMapping("/account/find-password")
    public String find_password_email(@RequestParam(value = "email") String email, Model model) {
        Account account = accountMapper.findByEmail(email);
        if(account == null) {
            model.addAttribute("error","wrong");
            return "account/find-password";
        }

        account.generatedFindPasswordToken();
        accountMapper.insertFindPasswordToken(account);
        accountService.sendMailFindPassword(account);

        model.addAttribute("message","Mail");
        return "account/done";
    }

    @GetMapping("/check-find-password")
    public String checkPassWordToken(String token, String email, Model model) {
        String view = "account/check-find-password";
        Account account = accountService.findByEmail(email);
        if(account == null) {
            model.addAttribute("error", "wrong.email");
            return view;
        }
        if(!account.isValidPasswordToken(token)) {
            model.addAttribute("error","wrong.token");
            return view;
        }

        model.addAttribute("email",email);
        model.addAttribute(new SignUpForm());
        return view;
    }

    @PostMapping("/account/change-password")
    public String submitPassword(@Valid @ModelAttribute SignUpForm signUpForm,
                                 Errors errors, Model model) {

        accountService.applyChangedPassword(signUpForm);
        model.addAttribute("message","Change");

        return "account/done";
    }
}
