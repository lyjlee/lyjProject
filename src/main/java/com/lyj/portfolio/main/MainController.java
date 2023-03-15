package com.lyj.portfolio.main;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyj.portfolio.CurrentAccount;
import com.lyj.portfolio.VO.Account;
import com.lyj.portfolio.VO.Board;
import com.lyj.portfolio.board.BoardService;
import com.lyj.portfolio.validator.SignUpFormValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final SignUpFormValidator signUpFormValidator;
    private final BoardService boardService;


    @GetMapping("/")
    public String home(@CurrentAccount Account account, Model model) {
        if(account != null) {
            model.addAttribute(account);
        }
        return "index";
    }

    @GetMapping("/board-index")
    public String goBoard(@CurrentAccount Account account, Model model,
                          @RequestParam(value = "pageNum") int pageNum) throws Exception {
        if(account != null) {
            model.addAttribute(account);
        }
        int page=pageNum;
        PageHelper.startPage(page,10);
        PageInfo<Board> pageInfo =
                new PageInfo<>(boardService.selectAllBoard(),10);
        model.addAttribute("boardList",pageInfo);
        return "board-index";
    }


    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
