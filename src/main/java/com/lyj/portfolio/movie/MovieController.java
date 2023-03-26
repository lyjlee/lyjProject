package com.lyj.portfolio.movie;


import com.lyj.portfolio.CurrentAccount;
import com.lyj.portfolio.VO.Account;
import com.lyj.portfolio.VO.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;
    @GetMapping("/movie-view")
    String viewMovie(@CurrentAccount Account account,
                     @RequestParam(value = "subject") String subject, Model model) {
        if(account != null) {
            model.addAttribute(account);
            Movie myComment = movieService.getMyComment(account.getUser_id(), subject);
            if(myComment == null) {
                model.addAttribute("myComment", null);
            }else {
                model.addAttribute("myComment", myComment);
            }
        }else {
            model.addAttribute("account",null);
        }

        List<Movie> movieInfoList = movieService.getMovieInfo(subject);
        Movie movie = movieService.selectMovies(subject);

        model.addAttribute(movieInfoList);
        model.addAttribute(movie);

        return "movie/movie-view";
    }


}
