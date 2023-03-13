package com.lyj.portfolio.board;

import com.lyj.portfolio.VO.Board;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface BoardRepository {
    @Select("SELECT * FROM BOARD ORDER BY board_no DESC")
    @Results(value = {
            @Result(property="no", column="board_no"),
            @Result (property="title", column="board_title"),
            @Result (property="description", column="board_des"),
            @Result (property="user_id", column="user_name"),
            @Result (property="submittedAt", column="board_submittedAt")
    })
    List<Board> seletAllBoard();

    Board findByNo(int no);

}
