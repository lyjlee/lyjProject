package com.lyj.portfolio.VO;

import lombok.*;

@Getter @Setter
@Builder @AllArgsConstructor @NoArgsConstructor
public class ReplyPageInfo {
    private int pageNum;
    private int numberOfLastPageReply;
}
