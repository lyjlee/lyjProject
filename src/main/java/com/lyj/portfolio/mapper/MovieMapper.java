package com.lyj.portfolio.mapper;

import com.lyj.portfolio.VO.Movie;
import org.apache.ibatis.annotations.*;

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

    @Select("SELECT * from mydb.movie_eval WHERE movie_subject=#{subject} AND movie_user_id=#{user_id}")
    @Results({
            @Result(property="score", column="movie_score"),
            @Result (property="comment", column="movie_comment"),
            @Result (property="user_id", column="movie_user_id"),
            @Result (property="submittedAt", column="comment_submittedat")
    })
    Movie getyMyComment(String user_id, String subject);

    @Update("UPDATE mydb.movie_eval SET movie_comment=#{movie.comment}, " +
            "movie_score=#{movie.score},comment_submittedat=current_timestamp " +
            "WHERE movie_subject=#{movie.subject} and movie_user_id=#{movie.user_id}")
    void editComment(@Param("movie") Movie movie);

    @Delete("DELETE FROM mydb.movie_eval WHERE movie_subject=#{subject} and movie_user_id=#{user_id}")
    void removeComment(String subject, String user_id);

    @Insert("INSERT INTO mydb.movie_eval(movie_subject, movie_score, movie_comment, movie_user_id, comment_submittedAt)" +
            " VALUES(#{movie.subject}, #{movie.score}, #{movie.comment},#{movie.user_id},current_timestamp)")
    void addComment(@Param("movie") Movie movie);

    @Select("SELECT D.movie_subject, D.movie_img, D.movie_genre, E.movie_score, E.movie_comment, E.movie_user_id, E.comment_submittedAt " +
            "FROM mydb.movie_db D INNER JOIN mydb.movie_eval E ON D.movie_subject = E.movie_subject and E.movie_user_id=#{userId}")
    @Results({
            @Result(property="subject", column="movie_subject"),
            @Result (property="img", column="movie_img"),
            @Result (property="genre", column="movie_genre"),
            @Result(property="score", column="movie_score"),
            @Result (property="comment", column="movie_comment"),
            @Result (property="user_id", column="movie_user_id"),
            @Result (property="submittedAt", column="comment_submittedat")
    })
    List<Movie> getMyComment(String userId);
}
