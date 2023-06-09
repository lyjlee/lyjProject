package com.lyj.portfolio.movie;

import com.lyj.portfolio.VO.Movie;
import com.lyj.portfolio.mapper.MovieMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MovieService {

    private final MovieMapper movieMapper;


    public List<Movie> selectAllMovies() {
        return movieMapper.selectAllMovies();
    }

    public List<Movie> getMovieEval(String subject) {
        return movieMapper.getMovieEval(subject);
    }

    public Movie selectMovies(String subject) {
        return movieMapper.selectMovies(subject);
    }

    public Movie getMyComment(String userId, String subject) {
        return movieMapper.getMyComment(userId, subject);
    }

    public void editComment(Movie movie) {
        movieMapper.editComment(movie);
    }

    public void removeComment(String subject, String userId) {
        movieMapper.removeComment(subject,userId);
        movieMapper.removeCommentLikes(subject, userId);
    }

    public void addComment(Movie movie) {
        movieMapper.addComment(movie);
    }

    // 좋아요 작업 처리
    public String likesComment(String userId, String hostId, String subject) {
        if(movieMapper.existsLikes(userId,hostId,subject)) {
            movieMapper.removeLikes(userId,hostId,subject);
            return "-1";
        }else {
            movieMapper.addLikes(userId,hostId,subject);
            return "1";
        }
    }
}
