package com.lyj.portfolio.board;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lyj.portfolio.VO.ReplyPageInfo;
import com.lyj.portfolio.VO.Reply;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReplyController {

    private final BoardService boardService;

    @RequestMapping(value = "/board/add-reply", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String newReply(@RequestParam("content") String content,
                           @RequestParam("no") int no,
                           @RequestParam("user_id") String user_id) {

        Reply replyDesc = boardService.createNewReply(content, no, user_id);
        ReplyPageInfo replyPageInfo = boardService.makePageInfo(replyDesc);

        Gson gson = new Gson();
        HashMap<String, Object> pageInfoList = new HashMap<String, Object>();
        pageInfoList.put("pageInfoList", replyPageInfo);

        return gson.toJson(pageInfoList);
    }

    @RequestMapping(value = "/board/add-n-reply", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String newNestedReply(@RequestParam("content") String content,
                                 @RequestParam("no") int no,
                                 @RequestParam("user_id") String user_id,
                                 @RequestParam("level") int level,
                                 @RequestParam("parentNo") int parentNo) {
        Reply newNestedReply = boardService.createNewNestedReply(content, no, user_id, level, parentNo);
        ReplyPageInfo replyPageInfo = boardService.makePageInfo(newNestedReply);

        Gson gson = new Gson();
        HashMap<String, Object> pageInfoList = new HashMap<String, Object>();
        pageInfoList.put("nestedReplyInfo", replyPageInfo);

        return gson.toJson(pageInfoList);
    }


    @RequestMapping(value = "/board/edit-reply", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String editReply(@RequestParam("content") String content,
                            @RequestParam("no") int no,
                            @RequestParam("user_id") String user_id,
                            @RequestParam("replyNo") int replyNo) {
        boardService.updateReply(content, no, user_id, replyNo );
        return null;
    }

    @RequestMapping(value = "/board/remove-reply", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String removeReply(@RequestParam("no") int no,
                              @RequestParam("user_id") String user_id,
                              @RequestParam("replyNo") int replyNo) {
        Reply reply = boardService.removeReply(no, user_id, replyNo);
        ReplyPageInfo replyPageInfo = boardService.makePageInfo(reply);

        Gson gson = new Gson();
        HashMap<String, Object> pageInfoList = new HashMap<String, Object>();
        pageInfoList.put("pageInfoList", replyPageInfo);

        return gson.toJson(pageInfoList);
    }



    @RequestMapping(value = "/board/view-reply", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
    @ResponseBody
    public String ReplyList(@RequestParam("no") int no,
                            @RequestParam("page") int page) {
        if(page == 0){
            page=1;
        }
        List<Reply> replyList = boardService.searchBoardReply(no,page);
        if (!replyList.isEmpty()) {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            return gson.toJson(replyList);
        }
        return null;
    }

}
