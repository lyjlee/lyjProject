package com.lyj.portfolio.VO;

import lombok.*;

@Getter @Setter
@Builder @AllArgsConstructor @NoArgsConstructor
public class PageInfo {
    private int pageNum;
    private int numberOfLastPageReply;
}
