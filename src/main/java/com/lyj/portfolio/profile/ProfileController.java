package com.lyj.portfolio.profile;

import com.lyj.portfolio.CurrentAccount;
import com.lyj.portfolio.VO.Account;
import com.lyj.portfolio.VO.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/profile-myPage")
    public String myPage(@CurrentAccount Account account, Model model) {
        List<Movie> myCommentList = profileService.getMyComment(account.getUser_id());
        model.addAttribute(account);
        model.addAttribute("myCommentList",myCommentList);
        return "profile/myPage";
    }
}
