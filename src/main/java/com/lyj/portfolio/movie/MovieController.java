package com.lyj.portfolio.movie;


import com.google.gson.Gson;
import com.lyj.portfolio.VO.Account;
import com.lyj.portfolio.VO.Movie;
import com.lyj.portfolio.login.AccountAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;
    @GetMapping("/movie-view")
    public String viewMovie(@AuthenticationPrincipal AccountAdapter user,
                     @RequestParam(value = "subject") String subject, Model model) {
        if(user != null) {
            model.addAttribute("account",user.getAccount());
            Movie myComment = movieService.getMyComment(user.getAccount().getUser_id(), subject);
            model.addAttribute("myComment", Objects.requireNonNullElseGet(myComment, Movie::new));
        }else {
            model.addAttribute("account",new Account());
            model.addAttribute("myComment", new Movie());
        }

        List<Movie> movieInfoList = movieService.getMovieEval(subject);
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
    public String addComment(@AuthenticationPrincipal AccountAdapter user, @Valid Movie movie,
                             HttpServletResponse response, Model model) throws IOException {
        response.setContentType("text/html; charset=UTF-8");

        if(user != null) {
            model.addAttribute("account",user.getAccount());
        }else {
            model.addAttribute("account",new Account());
        }

        movieService.addComment(movie);

//        PrintWriter out = response.getWriter();
//        out.println("<script>alert('한줄평이 등록 되었습니다'); location.href='redirect:/movie-view?subject="
//                + movie.getSubject() +"';</script>");
//        out.flush();
        return "redirect:/movie-view?subject=" + movie.getSubject();
    }

    @RequestMapping(value = "/comment-likes", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String likesComment(@RequestParam(value = "user_id") String user_id,
                               @RequestParam(value = "host_id") String host_id,
                               @RequestParam(value = "subject") String subject) {

        String likesValue = movieService.likesComment(user_id, host_id, subject);

        Gson gson = new Gson();
        HashMap<String, String> likes = new HashMap<String, String>();
        likes.put("likes", likesValue);

        return gson.toJson(likes);
    }

}
