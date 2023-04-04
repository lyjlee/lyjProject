package com.lyj.portfolio.profile;

import com.lyj.portfolio.CurrentAccount;
import com.lyj.portfolio.VO.Account;
import com.lyj.portfolio.VO.Movie;
import com.lyj.portfolio.account.AccountService;
import com.lyj.portfolio.account.SignUpForm;
import com.lyj.portfolio.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/profile-myPage")
    public String myPage(@CurrentAccount Account account, Model model, HttpServletRequest request) throws IOException {
        List<Movie> myCommentList = profileService.getMyComment(account.getUser_id());
        model.addAttribute(account);
        model.addAttribute("myCommentList",myCommentList);
        return "profile/myPage";
    }

    @GetMapping("/passCheck")
    public String passCheck(@CurrentAccount Account account, Model model) {
        model.addAttribute(account);
        return "profile/passCheck";
    }

    @PostMapping("/passCheck")
    public String passConfirm(@CurrentAccount Account account, Model model, @RequestParam(value = "password") String password) {
        model.addAttribute(account);
        if(accountService.checkPassword(password,account.getUser_id())) {
            model.addAttribute(new SignUpForm());
            return "profile/setting";
        }
        model.addAttribute("error","wrong");
        return "profile/passCheck";
    }

    @GetMapping("/profile")
    public String settingPage(@CurrentAccount Account account, Model model) {
        model.addAttribute(account);
        Account updatedAccount = accountMapper.findByUserId(account.getUser_id());
        model.addAttribute("updatedAccount", updatedAccount);
        return "profile/profile";
    }

    @PostMapping("/profile-setting")
    public String settingApply(@CurrentAccount Account account, @Valid @ModelAttribute SignUpForm signUpForm,
                               Errors errors, Model model) {
        model.addAttribute(account);
        Account updatedAccount = accountService.updateAccount(signUpForm, account.getUser_id());
        model.addAttribute("updatedAccount",updatedAccount);
        return "profile/profile";
    }
}
