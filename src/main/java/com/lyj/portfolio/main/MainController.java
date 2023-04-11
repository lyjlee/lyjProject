package com.lyj.portfolio.main;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyj.portfolio.VO.Account;
import com.lyj.portfolio.VO.Board;
import com.lyj.portfolio.VO.Movie;
import com.lyj.portfolio.board.BoardService;
import com.lyj.portfolio.login.AccountAdapter;
import com.lyj.portfolio.movie.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final BoardService boardService;
    private final MovieService movieService;

    @GetMapping("/")
    public String home(@AuthenticationPrincipal AccountAdapter user, Model model) {
        if(user != null) {
                model.addAttribute("account",user.getAccount());
        }else {
            model.addAttribute("account",new Account());
        }
        return "index";
    }

    @GetMapping("/board-index")
    public String goBoard(@AuthenticationPrincipal AccountAdapter user, Model model,
                          @RequestParam(value = "pageNum") int pageNum) throws Exception {
        if(user != null) {
            model.addAttribute("account",user.getAccount());
        }else {
            model.addAttribute("account",new Account());
        }
        int page=pageNum;
        PageHelper.startPage(page,10);
        PageInfo<Board> pageInfo =
                new PageInfo<>(boardService.selectAllBoard(),10);
        model.addAttribute("boardList",pageInfo);
        return "board-index";
    }

    @GetMapping("/movie-index")
    public String goMovie(@AuthenticationPrincipal AccountAdapter user, Model model) {
        if(user != null) {
            model.addAttribute("account",user.getAccount());
        }else {
            model.addAttribute("account",new Account());
        }
        List<Movie> movies = movieService.selectAllMovies();
        model.addAttribute("movies",movies);
        return "movie-index";
    }
}
