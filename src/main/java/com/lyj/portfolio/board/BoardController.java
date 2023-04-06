package com.lyj.portfolio.board;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.JsonObject;
import com.lyj.portfolio.VO.Account;
import com.lyj.portfolio.VO.Board;
import com.lyj.portfolio.VO.ReplyPageInfo;
import com.lyj.portfolio.VO.Reply;
import com.lyj.portfolio.login.AccountAdapter;
import com.lyj.portfolio.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final BoardMapper boardMapper;

    @GetMapping("/new-board")
    public String newBoardForm(@AuthenticationPrincipal AccountAdapter user, Model model) {
        if(user != null) {
            model.addAttribute("account",user.getAccount());
        }else {
            model.addAttribute("account",new Account());
        }
        model.addAttribute(new BoardForm());
        return "board/board-form";
    }

    @PostMapping("/new-board")
    public String newBoardSubmit(@AuthenticationPrincipal AccountAdapter user, @Valid BoardForm boardForm) {
        boardService.createNewBoard(boardForm, user.getAccount());
        return "redirect:/board-index?pageNum=1";
    }

    @GetMapping("/mod-board")
    public String modifyBoard(@AuthenticationPrincipal AccountAdapter user , Model model,
                              @RequestParam(value = "no") int no) {

        Board board = boardService.viewBoard(no);
        model.addAttribute(board);

        if(user != null) {
            model.addAttribute("account",user.getAccount());
        }else {
            model.addAttribute("account",new Account());
        }

        if(board == null) {
            return "error";
        }

        if(!(user.getAccount().getUser_id().equals(board.getUser_id()))) {
            model.addAttribute("error","wrong.request");
            return "board/board-view";
        }
        model.addAttribute(new BoardForm());
        return "board/board-modify";
    }

    @PostMapping("/mod-board")
    public String modifyBoardSubmit(@AuthenticationPrincipal AccountAdapter user , @Valid BoardForm boardForm,
                                    Model model, @RequestParam(value = "no") int no) throws IOException {

        Board updatedBoard = boardService.updateBoard(boardForm, user.getAccount(),no);
        model.addAttribute("board",updatedBoard);
        model.addAttribute("account",user.getAccount());

        return "redirect:/board-view?no=" + no;
    }

    @GetMapping("/remove-board")
    public String removeBoard(@AuthenticationPrincipal AccountAdapter user, @RequestParam(value = "no") int no,
                              Model model, HttpServletResponse response) throws IOException {

        response.setContentType("text/html; charset=UTF-8");
        Board board = boardService.viewBoard(no);
            if(user == null || board == null) {
                model.addAttribute("error","wrong.request");
                return "redirect:/board-index";
            }
        boardService.removeBoard(no);
        model.addAttribute("account",user.getAccount());

        return "redirect:/board-index?pageNum=1";
    }

    @GetMapping("/search-result")
    String searchBoardIndex(@AuthenticationPrincipal AccountAdapter user,
                            @RequestParam(value = "search") String keyWord,
                            @RequestParam(value = "pageNum") int pageNum,
                            Model model) {
        int page=pageNum;
        PageHelper.startPage(page,10);
        PageInfo<Board> pageInfo =
                new PageInfo<>(boardService.searchBoard(keyWord),10);
        model.addAttribute("boardList",pageInfo);

        if(user != null) {
            model.addAttribute("account",user.getAccount());
        }else {
            model.addAttribute("account",new Account());
        }
        model.addAttribute("search",keyWord);
        return "board/search-result";
    }


    @GetMapping("/board-view")
    public String viewSelectedBoard(@AuthenticationPrincipal AccountAdapter user,
                                    Model model,
                                    @RequestParam(value = "no") int no,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {
        //조회수+중복방지 구현
        Cookie oldcookie = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for(Cookie cookie : cookies) {
                if(cookie.getName().equals("boardHits")) {
                    oldcookie = cookie;
                }
            }
        }
        if (oldcookie != null) {
            if(!oldcookie.getValue().contains("["+no+"]")) {
                boardService.boardViewCount(no);
                oldcookie.setValue(oldcookie.getValue() + "["+no+"]");
                oldcookie.setPath("/");
                oldcookie.setMaxAge(60*60*2);
                response.addCookie(oldcookie);
            }
        }else {
            boardService.boardViewCount(no);
            Cookie newCookie = new Cookie("boardHits","["+no+"]");
            newCookie.setPath("/");
            newCookie.setMaxAge(60*60*2);
            response.addCookie(newCookie);
        }

        if(user != null) {
            model.addAttribute("account",user.getAccount());
        }else {
            model.addAttribute("account",new Account());
        }
        Board board = boardService.viewBoard(no);
        int page = board.getRow()/10;
        int remain = page%10;
        if(page == 0) {
            page++;
        }else {
            if(remain != 0) {
                page++;
            }
        }
        model.addAttribute(board);
        model.addAttribute("pageNum",page);
        model.addAttribute("pageRNo",1);


        Reply reply = boardMapper.selectDESCNewReply(no);
        if(reply != null) {
            ReplyPageInfo replyPageInfo = boardService.makePageInfo(reply);
            model.addAttribute("lastRPage", replyPageInfo.getPageNum());
        }else {
            model.addAttribute("lastRPage", 0);
        }

        return "board/board-view";
    }

    @RequestMapping(value="/uploadSummernoteImageFile", produces = "application/json; charset=utf8")
    @ResponseBody
    public String uploadSummernoteImageFile(@RequestParam("file") MultipartFile multipartFile, HttpServletRequest request )  {
        JsonObject jsonObject = new JsonObject();

        String fileRoot = "C:\\summernote_image\\"; // 외부경로로 저장

        // 내부경로로 저장
        String contextRoot = new HttpServletRequestWrapper(request).getRealPath("/");
//        String fileRoot = contextRoot+"resources/static/images/";

        String originalFileName = multipartFile.getOriginalFilename();	//오리지날 파일명
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));	//파일 확장자
        String savedFileName = UUID.randomUUID() + extension;	//저장될 파일명

        File targetFile = new File(fileRoot + savedFileName);
        try {
            InputStream fileStream = multipartFile.getInputStream();
            FileUtils.copyInputStreamToFile(fileStream, targetFile);	//파일 저장
            jsonObject.addProperty("url", "/summernoteImage/"+savedFileName); // contextroot + resources + 저장할 내부 폴더명
            jsonObject.addProperty("responseCode", "success");

        } catch (IOException e) {
            FileUtils.deleteQuietly(targetFile);	//저장된 파일 삭제
            jsonObject.addProperty("responseCode", "error");
            e.printStackTrace();
        }
        String a = jsonObject.toString();
        return a;
    }


    }
