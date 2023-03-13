package com.lyj.portfolio.VO;

import lombok.*;

import java.sql.Timestamp;

@Getter @Setter
@Builder @AllArgsConstructor @NoArgsConstructor
public class Reply {
    private int row;
    private int No;
    private int Parent_No;
    private int level;
    private int BoardNo;
    private String content;
    private String user_id;
    private Timestamp submittedAt;
}
