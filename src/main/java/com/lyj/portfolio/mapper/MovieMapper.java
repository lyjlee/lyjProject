package com.lyj.portfolio.mapper;

import com.lyj.portfolio.VO.Movie;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MovieMapper {

    //영화 정보 + 평균 별점 (board-index 에 출력할 데이터)
    @Select("SELECT A.movie_subject, A.movie_img, A.movie_genre," +
            "(SELECT avg(movie_score) as avg_score FROM mydb.movie_eval WHERE A.movie_subject = mydb.movie_eval.movie_subject) " +
            "FROM mydb.movie_db A")
    @Results({
            @Result(property="subject", column="movie_subject"),
            @Result (property="img", column="movie_img"),
            @Result (property="genre", column="movie_genre"),
            @Result (property="avg_score", column="avg_score")
    })
    List<Movie> selectAllMovies();

    @Select("SELECT * from mydb.movie_eval WHERE movie_subject=#{subject}")
    @Results({
            @Result(property="score", column="movie_score"),
            @Result (property="comment", column="movie_comment"),
            @Result (property="user_id", column="movie_user_id"),
            @Result (property="submittedAt", column="comment_submittedat")
    })
    List<Movie> getMovieEval(String subject);

    @Select("SELECT movie_subject, movie_img, movie_genre, " +
            "(SELECT avg(movie_score) from mydb.movie_eval where movie_subject=#{subject}) as avg_score " +
            "FROM mydb.movie_db WHERE movie_subject=#{subject}")
    @Results({
            @Result(property="subject", column="movie_subject"),
            @Result (property="img", column="movie_img"),
            @Result (property="genre", column="movie_genre"),
            @Result (property="avg_score", column="avg_score")
    })
    Movie selectMovies(String subject);

    @Select("SELECT * from mydb.movie_eval WHERE movie_subject=#{subject} AND movie_user_id=#{userId}")
    @Results({
            @Result(property="score", column="movie_score"),
            @Result (property="comment", column="movie_comment"),
            @Result (property="user_id", column="movie_user_id"),
            @Result (property="submittedAt", column="comment_submittedat")
    })
    Movie getyMyComment(String userId, String subject);

}
