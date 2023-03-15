package com.lyj.portfolio.mapper;

import com.lyj.portfolio.VO.Board;
import com.lyj.portfolio.VO.Reply;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BoardMapper {

//    게시글 등록
    @Insert("INSERT INTO board(board_title, board_des, user_name, board_submittedAt)" +
            " VALUES(#{board.title}, #{board.description}, #{board.user_id},current_timestamp)")
    void submitNewBoard(@Param("board") Board board);

// 새 페이징용 board 불러오기
    @Select("SELECT * FROM BOARD ORDER BY board_no DESC")
    @Results(value = {
            @Result(property="no", column="board_no"),
            @Result (property="title", column="board_title"),
            @Result (property="description", column="board_des"),
            @Result (property="user_id", column="user_name"),
            @Result (property="submittedAt", column="board_submittedAt"),
            @Result (property="view", column="board_view")
    })
    List<Board> selectAllBoard();

    @Select("SELECT board_no, board_title, board_des, user_name, board_submittedAt, board_view " +
            "FROM Board as B WHERE B.board_title like '%${keyWord}%' OR B.board_des like '%${keyWord}%' ORDER BY board_no DESC")
    @Results({
            @Result(property="no", column="board_no"),
            @Result (property="title", column="board_title"),
            @Result (property="description", column="board_des"),
            @Result (property="user_id", column="user_name"),
            @Result (property="submittedAt", column="board_submittedAt"),
            @Result (property="view", column="board_view")
    })
    List<Board> searchBoard(String keyWord);

    @Update("UPDATE Board SET board_view=board_view+1 WHERE board_no=#{no}")
    void viewCount(int no);





    //    게시글 내용 읽어오기 (게시글 번호로)
    //@Select("SELECT * from BOARD WHERE board_no = #{no}")

    //이전 페이지의 목록 추가 버전
    @Select("SELECT row, board_no, board_title,board_des, user_name, board_submittedAt FROM " +
            "(SELECT row_number() over (order by board_no DESC) as row, board_no, board_title,board_des, user_name, board_submittedAt " +
            "FROM Board) as B WHERE B.board_no=#{no}")
    @Results({
            @Result(property="row", column="row"),
            @Result(property="no", column="board_no"),
            @Result (property="title", column="board_title"),
            @Result (property="description", column="board_des"),
            @Result (property="user_id", column="user_name"),
            @Result (property="submittedAt", column="board_submittedAt")
    })
    Board findByNo(int no);

//    게시글 수정
    @Update("UPDATE BOARD SET board_title=#{board.title}, board_des=#{board.description}," +
            " user_name=#{board.user_id}, board_submittedAt=current_timestamp WHERE user_name = #{board.user_id}")
    void submitUpdateBoard(@Param("board") Board board);


//    게시글 삭제
    @Delete("DELETE FROM BOARD WHERE board_no = #{no}")
    boolean removeBoard(int no);


/*Todo 댓글 불러오기 (기존 버전)*/

//    @Select("SELECT * FROM REPLY WHERE board_no=#{no} ORDER BY reply_no ASC")
//    @Results(value = {
//            @Result(property="no", column="reply_no"),
//            @Result (property="BoardNo", column="board_no"),
//            @Result (property="content", column="reply_content"),
//            @Result (property="user_id", column="user_name"),
//            @Result (property="submittedAt", column="reply_submittedAt")
//    })
//    List<Reply> selectAllReply(int no);


//    댓글 불러오기 (대댓글 추가 버전)
    @Select("WITH RECURSIVE search_reply" +
            "(reply_no, reply_content, reply_parent_no, user_name, reply_submittedAt, level, reply_path, cycle) AS (" +
            "SELECT r.reply_no, r.reply_content, r.reply_parent_no, r.user_name, r.reply_submittedAt, 1, array[r.reply_no], false " +
            "FROM reply r WHERE r.reply_parent_no=0 and r.board_no=#{no} " +
            "UNION ALL " +
            "SELECT r.reply_no,r.reply_content,r.reply_parent_no,r.user_name,r.reply_submittedAt,level+1,reply_path||r.reply_no,r.reply_no=any(reply_path) " +
            "FROM reply r, search_reply sr WHERE r.reply_parent_no=sr.reply_no and NOT cycle) " +
            "SELECT reply_no,lpad('',level)||reply_content AS reply_content,reply_parent_no,user_name,reply_submittedAt,level AS reply_level,reply_path " +
            "FROM search_reply ORDER BY reply_path " +
            "offset (#{page}-1)*10 FETCH FIRST 10 ROWS ONLY")
    @Results(value = {
            @Result(property="no", column="reply_no"),
            @Result(property="level", column="reply_level"),
            @Result(property="Parent_No", column="reply_parent_no"),
            @Result (property="BoardNo", column="board_no"),
            @Result (property="content", column="reply_content"),
            @Result (property="user_id", column="user_name"),
            @Result (property="submittedAt", column="reply_submittedAt")
    })
    List<Reply> selectAllReply(int no, int page);

// 삭제 대상 댓글에 대댓글 존재하는지 여부 확인
    @Select("SELECT EXISTS (SELECT * FROM REPLY WHERE reply_parent_no=#{replyNo} and board_no=#{no})")
    boolean checkNestedReply(int no, int replyNo);


//    댓글 등록
    @Insert("INSERT INTO REPLY(board_no, user_name, reply_content, reply_submittedAt)" +
            " VALUES(#{no}, #{userId}, #{content},current_timestamp)")
    void submitNewReply(String content, int no, String userId);

//     페이징 위한 최신댓글순 정렬 + row_number 받아오기
    @Select("WITH RECURSIVE search_reply" +
            "(reply_no, reply_content, reply_parent_no, user_name, reply_submittedAt, level, reply_path, cycle) AS (" +
            "SELECT r.reply_no, r.reply_content, r.reply_parent_no, r.user_name, r.reply_submittedAt, 1, array[r.reply_no], false " +
            "FROM reply r WHERE r.reply_parent_no=0 and r.board_no=#{no} " +
            "UNION ALL " +
            "SELECT r.reply_no,r.reply_content,r.reply_parent_no,r.user_name,r.reply_submittedAt,level+1,reply_path||r.reply_no,r.reply_no=any(reply_path) " +
            "FROM reply r, search_reply sr WHERE r.reply_parent_no=sr.reply_no and NOT cycle) " +
            "select row, reply_no, user_name, reply_content FROM "  +
            "(SELECT row_number() over (order by reply_path) as row, reply_no,lpad('',level)||reply_content AS reply_content,reply_parent_no,user_name,reply_submittedAt,level AS reply_level,reply_path " +
            "FROM search_reply ORDER BY row DESC) as R " +
            "limit 1")
    @Results(value = {
            @Result(property="row", column="row"),
            @Result(property="no", column="reply_no"),
            @Result(property="level", column="reply_level"),
            @Result(property="Parent_No", column="reply_parent_no"),
            @Result (property="BoardNo", column="board_no"),
            @Result (property="content", column="reply_content"),
            @Result (property="user_id", column="user_name"),
            @Result (property="submittedAt", column="reply_submittedAt")
    })
    Reply selectDESCNewReply(int no);



    //    댓글 수정
    @Update("UPDATE REPLY SET reply_content=#{content} WHERE reply_no=#{replyNo} and board_no=#{no} and user_name=#{userId}")
    void submitEditReply(String content, int no, String userId, int replyNo);

//    댓글 삭제
    @Delete("DELETE FROM REPLY WHERE reply_no=#{replyNo} and board_no=#{no} and user_name=#{userId}")
    void removeReply(int no, String userId, int replyNo);

//    대댓글 존재 댓글 삭제처리
    @Update("UPDATE REPLY SET reply_content='삭제된 댓글입니다', user_name='null' WHERE reply_no=#{replyNo} and board_no=#{no}")
    void removeTempRReply(int no, int replyNo);


    //    대댓글 등록
    @Insert("INSERT INTO REPLY(board_no, user_name, reply_content, reply_submittedAt, reply_level, reply_parent_no)" +
            " VALUES(#{no}, #{userId}, #{content},current_timestamp, #{level}, #{parentNo})")
    void submitNewNestedReply(String content, int no, String userId, int level, int parentNo);



    //    직전 등록한 댓글의 row + 데이터 가져오기
    @Select("WITH RECURSIVE search_reply" +
            "(reply_no, reply_content, reply_parent_no, user_name, reply_submittedAt, level, reply_path, cycle) AS (" +
            "SELECT r.reply_no, r.reply_content, r.reply_parent_no, r.user_name, r.reply_submittedAt, 1, array[r.reply_no], false " +
            "FROM reply r WHERE r.reply_parent_no=0 and r.board_no=#{no} " +
            "UNION ALL " +
            "SELECT r.reply_no,r.reply_content,r.reply_parent_no,r.user_name,r.reply_submittedAt,level+1,reply_path||r.reply_no,r.reply_no=any(reply_path) " +
            "FROM reply r, search_reply sr WHERE r.reply_parent_no=sr.reply_no and NOT cycle) " +
            "select row, reply_no, user_name, reply_content FROM "  +
            "(SELECT row_number() over (order by reply_path) as row, reply_no,lpad('',level)||reply_content AS reply_content,reply_parent_no,user_name,reply_submittedAt,level AS reply_level,reply_path " +
            "FROM search_reply ORDER BY row DESC) as R " +
            "WHERE R.reply_level=#{level} AND R.reply_parent_no=#{parentNo} " +
            "limit 1")
    @Results(value = {
            @Result(property="row", column="row"),
            @Result(property="no", column="reply_no"),
            @Result(property="level", column="reply_level"),
            @Result(property="Parent_No", column="reply_parent_no"),
            @Result (property="BoardNo", column="board_no"),
            @Result (property="content", column="reply_content"),
            @Result (property="user_id", column="user_name"),
            @Result (property="submittedAt", column="reply_submittedAt")
    })
    Reply getNestedReply(int level, int parentNo, int no);
}
