package com.lyj.portfolio.board;

import com.lyj.portfolio.VO.Account;
import com.lyj.portfolio.VO.Board;
import com.lyj.portfolio.VO.ReplyPageInfo;
import com.lyj.portfolio.VO.Reply;
import com.lyj.portfolio.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardMapper boardMapper;

    public void createNewBoard(BoardForm boardForm, Account account) {
        Board board = Board.builder()
                .user_id(account.getUser_id())
                .title(boardForm.getTitle())
                .description(boardForm.getDescription())
                .build();

        boardMapper.submitNewBoard(board);
    }


    public List<Board> selectAllBoard() {
        return boardMapper.selectAllBoard();
    }

    public Board viewBoard(int no) {
        return boardMapper.findByNo(no);
    }

    public Board updateBoard(BoardForm boardForm, Account account,int no) {
        Board board = Board.builder()
                .user_id(account.getUser_id())
                .title(boardForm.getTitle())
                .description(boardForm.getDescription())
                .build();
        boardMapper.submitUpdateBoard(board);
        return viewBoard(no);
    }

    public boolean removeBoard(int no) {
        return boardMapper.removeBoard(no);
    }

    public void boardViewCount(int no) {
        boardMapper.viewCount(no);
    }

    public List<Board> searchBoard(String keyWord) {
        return boardMapper.searchBoard(keyWord);
    }

    public List<Reply> searchBoardReply(int no, int page) {
        List<Reply> replyIndex = boardMapper.selectAllReply(no, page);
        return replyIndex;
    }

    public Reply createNewReply(String content, int no, String user_id) {
        boardMapper.submitNewReply(content, no, user_id);
        return boardMapper.selectDESCNewReply(no);
    }

    //답 댓글 생성
    public Reply createNewNestedReply(String content, int no, String user_id, int level, int parentNo) {
        boardMapper.submitNewNestedReply(content, no, user_id, level, parentNo);
        return boardMapper.getNestedReply(level, parentNo, no);

    }

    public void updateReply(String content, int no, String user_Id, int replyNo) {
        boardMapper.submitEditReply(content, no, user_Id, replyNo);
    }

    public Reply removeReply(int no, String userId, int replyNo) {

        if(boardMapper.checkNestedReply(no,replyNo)) {
            boardMapper.removeTempRReply(no, replyNo);
        }else {
            boardMapper.removeReply(no, userId, replyNo);
        }
        return boardMapper.selectDESCNewReply(no);
    }

//  댓글용 페이징처리 메소드
    public ReplyPageInfo makePageInfo(Reply replyDesc) {
        if(replyDesc != null) {
            int newReplyRowNumber = replyDesc.getRow();
            int page = newReplyRowNumber / 10;
            int numberOfLastPageReply = newReplyRowNumber % 10;
            if (numberOfLastPageReply != 0) {
                page++;
            }
            ReplyPageInfo replyPageInfo = new ReplyPageInfo();
            replyPageInfo.setPageNum(page);
            replyPageInfo.setNumberOfLastPageReply(numberOfLastPageReply);

            return replyPageInfo;
        }
        ReplyPageInfo replyPageInfo = new ReplyPageInfo();
        replyPageInfo.setPageNum(0);
        replyPageInfo.setNumberOfLastPageReply(0);

        return replyPageInfo;
    }
}
