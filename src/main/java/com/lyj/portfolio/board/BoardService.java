package com.lyj.portfolio.board;

import com.lyj.portfolio.VO.Account;
import com.lyj.portfolio.VO.Board;
import com.lyj.portfolio.VO.PageInfo;
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

    public List<Board> searchBoardindex(int no) throws Exception {
        List<Board> boardindex = boardMapper.seletAllBoard(no);
        return boardindex;
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

    public List<Board> searchBoard(String keyWord, int no) {
        return boardMapper.searchBoardByKeyWord(keyWord, no);
    }

    public List<Reply> searchBoardReply(int no, int page) {
        List<Reply> replyIndex = boardMapper.selectAllReply(no, page);
        return replyIndex;
    }

    public Reply createNewReply(String content, int no, String user_id) {
        boardMapper.submitNewReply(content, no, user_id);
        return boardMapper.selectDESCNewReply(no);
    }
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


    public PageInfo makePageInfo(Reply replyDesc) {
        if(replyDesc != null) {
            int newReplyRowNumber = replyDesc.getRow();
            int page = newReplyRowNumber / 10;
            int numberOfLastPageReply = newReplyRowNumber % 10;
            if (numberOfLastPageReply != 0) {
                page++;
            }
            PageInfo pageInfo = new PageInfo();
            pageInfo.setPageNum(page);
            pageInfo.setNumberOfLastPageReply(numberOfLastPageReply);

            return pageInfo;
        }
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageNum(0);
        pageInfo.setNumberOfLastPageReply(0);

        return pageInfo;
    }

    public int searchLastBoardPage() {
        int lastBoard = boardMapper.selectLastBoard();

        if(lastBoard != 0) {
            int lastBoardNo = lastBoard;
            int page = lastBoardNo / 10;
            int numberOfLastPageReply = lastBoardNo % 10;
            if (numberOfLastPageReply != 0) {
                page++;
            }

            return page;
        }
        return 1;
    }


    public void boardViewCount(int no) {
        boardMapper.viewCount(no);
    }
}
