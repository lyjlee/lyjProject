package com.lyj.portfolio.VO;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter @Setter
@Builder @AllArgsConstructor @NoArgsConstructor
public class Board {
    private int row;
    private int no;
    private String title;
    private String description;
    private String user_id;
    private Timestamp submittedAt;
    private int view;
}
