package com.lyj.portfolio.profile;

import com.lyj.portfolio.VO.Account;
import com.lyj.portfolio.VO.Movie;
import com.lyj.portfolio.account.AccountService;
import com.lyj.portfolio.account.SignUpForm;
import com.lyj.portfolio.login.AccountAdapter;
import com.lyj.portfolio.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final AccountMapper accountMapper;
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;


    @GetMapping("/profile-myPage")
    public String myPage(@AuthenticationPrincipal AccountAdapter user, Model model, HttpServletRequest request) throws IOException {
        List<Movie> myCommentList = profileService.getMyCommentOnMyPage(user.getAccount().getUser_id());
        model.addAttribute(user.getAccount());
        model.addAttribute("myCommentList",myCommentList);
        return "profile/myPage";
    }

    @GetMapping("/passCheck")
    public String passCheck(@AuthenticationPrincipal AccountAdapter user, Model model) {
        model.addAttribute(user.getAccount());
        return "profile/passCheck";
    }

    @PostMapping("/passCheck")
    public String passConfirm(@AuthenticationPrincipal AccountAdapter user, Model model, @RequestParam(value = "password") String password) {
        model.addAttribute(user.getAccount());
        if(accountService.checkPassword(password,user.getAccount().getUser_id())) {
            model.addAttribute(new SignUpForm());
            return "profile/setting";
        }
        model.addAttribute("error","wrong");
        return "profile/passCheck";
    }

    @GetMapping("/profile")
    public String settingPage(@AuthenticationPrincipal AccountAdapter user, Model model) {
        model.addAttribute(user.getAccount());
        Account updatedAccount = accountMapper.findByUserId(user.getAccount().getUser_id());
        model.addAttribute("updatedAccount", updatedAccount);
        return "profile/profile";
    }

    @PostMapping("/profile-setting")
    public String settingApply(@AuthenticationPrincipal AccountAdapter user, @Valid @ModelAttribute SignUpForm signUpForm,
                               Errors errors, Model model) {
        model.addAttribute(user.getAccount());

        if(signUpForm.getPassword() != null) {
            signUpForm.setPassword(passwordEncoder.encode(signUpForm.getPassword()));
        }
        Account updatedAccount = accountService.updateAccount(signUpForm, user.getAccount().getUser_id());
        model.addAttribute("updatedAccount",updatedAccount);
        return "profile/profile";
    }
}
