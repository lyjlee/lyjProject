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

    //좋아요 개수 포함한 한줄평 전부 가져오기
    @Select("SELECT A.movie_score, A.movie_comment, A.movie_user_id, A.comment_submittedat, " +
            "(SELECT COALESCE(count(movie_likes.movie_likes), '0') as comment_likes " +
            "FROM mydb.movie_likes WHERE movie_host_id=A.movie_user_id AND movie_subject=#{subject}) " +
            "FROM mydb.movie_eval A WHERE movie_subject=#{subject}")
    @Results({
            @Result(property="score", column="movie_score"),
            @Result (property="comment", column="movie_comment"),
            @Result (property="user_id", column="movie_user_id"),
            @Result (property="submittedAt", column="comment_submittedat"),
            @Result (property="likes", column="comment_likes")
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


    //좋아요 개수 포함한 내 한줄평 가져오기
    @Select("SELECT A.movie_score, A.movie_comment, A.movie_user_id, A.comment_submittedat, " +
            "(SELECT COALESCE(count(movie_likes.movie_likes), '0') as comment_likes " +
            "FROM mydb.movie_likes WHERE movie_host_id=A.movie_user_id AND movie_subject=#{subject}) " +
            "FROM mydb.movie_eval A WHERE movie_subject=#{subject} AND movie_user_id=#{user_id}")
    @Results({
            @Result(property="score", column="movie_score"),
            @Result (property="comment", column="movie_comment"),
            @Result (property="user_id", column="movie_user_id"),
            @Result (property="submittedAt", column="comment_submittedat"),
            @Result (property="likes", column="comment_likes")
    })
    Movie getMyComment(String user_id, String subject);

    @Update("UPDATE mydb.movie_eval SET movie_comment=#{movie.comment}, " +
            "movie_score=#{movie.score},comment_submittedat=current_timestamp " +
            "WHERE movie_subject=#{movie.subject} and movie_user_id=#{movie.user_id}")
    void editComment(@Param("movie") Movie movie);

    @Delete("DELETE FROM mydb.movie_eval WHERE movie_subject=#{subject} and movie_user_id=#{user_id}")
    void removeComment(String subject, String user_id);

    //한줄평 삭제시 동시에 좋아요도 제거
    @Delete("DELETE FROM mydb.movie_likes WHERE movie_subject=#{subject} AND movie_host_id=#{userId}")
    void removeCommentLikes(String subject, String userId);

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
    List<Movie> getMyCommentOnMyPage(String userId);

    //좋아요 클릭 여부
    @Select("SELECT EXISTS (SELECT 1 FROM mydb.movie_likes " +
            "WHERE movie_subject = #{subject} AND movie_host_id = #{hostId} AND movie_user_id = #{userId})")
    boolean existsLikes(String userId, String hostId, String subject);

    //좋아요 추가
    @Insert("INSERT INTO mydb.movie_likes(movie_subject, movie_host_id, movie_user_id, movie_likes)" +
            " VALUES(#{subject},#{hostId},#{userId},1)")
    void addLikes(String userId, String hostId, String subject);

    //좋아요 제거
    @Delete("DELETE FROM mydb.movie_likes WHERE movie_subject=#{subject} AND movie_user_id=#{userId} AND movie_host_id=#{hostId}")
    void removeLikes(String userId,String hostId,String subject);



}
