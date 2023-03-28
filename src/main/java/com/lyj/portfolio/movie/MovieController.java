package com.lyj.portfolio.movie;


import com.google.gson.Gson;
import com.lyj.portfolio.CurrentAccount;
import com.lyj.portfolio.VO.Account;
import com.lyj.portfolio.VO.Movie;
import com.lyj.portfolio.board.BoardForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;
    @GetMapping("/movie-view")
    public String viewMovie(@CurrentAccount Account account,
                     @RequestParam(value = "subject") String subject, Model model) {
        if(account != null) {
            model.addAttribute(account);
            Movie myComment = movieService.getMyComment(account.getUser_id(), subject);
            if(myComment == null) {
                model.addAttribute("myComment", new Movie());
            }else {
                model.addAttribute("myComment", myComment);
            }
        }else {
            model.addAttribute("account",new Account());
            model.addAttribute("myComment", new Movie());
        }

        List<Movie> movieInfoList = movieService.getMovieInfo(subject);
        Movie movie = movieService.selectMovies(subject);

        model.addAttribute(movieInfoList);
        model.addAttribute(movie);
        model.addAttribute("commentForm",new Movie());
        return "movie/movie-view";
    }

    //코멘트 수정
    @RequestMapping(value = "/edit-comment", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String editComment(@RequestParam(value = "subject") String subject,
                       @RequestParam(value = "comment") String comment,
                       @RequestParam(value = "score") int score,
                       @RequestParam(value = "user_id") String user_id) {
        Movie movie = new Movie();
        movie.setSubject(subject);
        movie.setComment(comment);
        movie.setScore(score);
        movie.setUser_id(user_id);
        movieService.editComment(movie);

        Gson gson = new Gson();
        HashMap<String, String> message = new HashMap<String, String>();
        message.put("message", "수정이 완료되었습니다.");

        return gson.toJson(message);
    }

    //코멘트 삭제
    @RequestMapping(value = "/remove-comment", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String removeComment(@RequestParam(value = "subject") String subject,
                                @RequestParam(value = "user_id") String user_id) {

        movieService.removeComment(subject, user_id);


        Gson gson = new Gson();
        HashMap<String, String> message = new HashMap<String, String>();
        message.put("message", "삭제가 완료되었습니다.");

        return gson.toJson(message);
    }

    @PostMapping("/add-comment")
    public String addComment(@CurrentAccount Account account, @Valid Movie movie,
                             HttpServletResponse response, Model model) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        if(account != null) {
            model.addAttribute(account);
        }
        movieService.addComment(movie);

        PrintWriter out = response.getWriter();
        // 등록 후 리다이렉트 수정 요망.
        out.println("<script>alert('한줄평이 등록 되었습니다'); location.href='redirect:/movie-view?subject="
                + movie.getSubject() +"';</script>");
        out.flush();
        return "redirect:/movie-view?subject=" + movie.getSubject();
    }

}
